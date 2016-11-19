package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import static com.github.marschall.threeten.jpa.jdbc42.hibernate.Constants.PERSISTENCE_UNIT_NAME;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.H2Configuration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.HibernateConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.PostgresConfiguration;

@RunWith(Parameterized.class)
public class ConverterWithTimeZoneTest {

  private final Class<?> datasourceConfiguration;
  private final Class<?> jpaConfiguration;
  private final String persistenceUnitName;
  private AnnotationConfigApplicationContext applicationContext;
  private TransactionTemplate template;

  public ConverterWithTimeZoneTest(Class<?> datasourceConfiguration, Class<?> jpaConfiguration, String persistenceUnitName) {
    this.datasourceConfiguration = datasourceConfiguration;
    this.jpaConfiguration = jpaConfiguration;
    this.persistenceUnitName = persistenceUnitName;
  }

  @Parameters(name = "{2}")
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
            //new Object[]{HsqlConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-hsql"},
            //new Object[]{SqlServerConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-sqlserver"},
            new Object[]{H2Configuration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-h2"},
            new Object[]{PostgresConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-postgres"}
            );
  }

  @Before
  public void setUp() {
    this.applicationContext = new AnnotationConfigApplicationContext();
    this.applicationContext.register(this.datasourceConfiguration, TransactionManagerConfiguration.class, this.jpaConfiguration);
    ConfigurableEnvironment environment = this.applicationContext.getEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map<String, Object> source = singletonMap(PERSISTENCE_UNIT_NAME, this.persistenceUnitName);
    propertySources.addFirst(new MapPropertySource("persistence unit name", source));
    this.applicationContext.refresh();

    PlatformTransactionManager txManager = this.applicationContext.getBean(PlatformTransactionManager.class);
    this.template = new TransactionTemplate(txManager);

    this.template.execute(status -> {
      Map<String, DatabasePopulator> beans = this.applicationContext.getBeansOfType(DatabasePopulator.class);
      DataSource dataSource = this.applicationContext.getBean(DataSource.class);
      try (Connection connection = dataSource.getConnection()) {
        for (DatabasePopulator populator : beans.values()) {
          populator.populate(connection);
        }
      } catch (SQLException e) {
        throw new RuntimeException("could initialize database", e);
      }
      return null;
    });
  }

  @After
  public void tearDown() {
    this.applicationContext.close();
  }

  @Test
  public void runTest() {
    EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
    EntityManager entityManager = factory.createEntityManager();
    try {
      // read the entity inserted by SQL
      this.template.execute((s) -> {
        Query query = entityManager.createQuery("SELECT t FROM JavaTime42WithZone t");
        List<?> resultList = query.getResultList();
        assertThat(resultList, hasSize(1));

        // validate the entity inserted by SQL
        JavaTime42WithZone javaTime = (JavaTime42WithZone) resultList.get(0);
//        assertEquals(ZonedDateTime.parse("1960-01-01T23:03:20-05:00[America/New_York]"), javaTime.getZoned());
//        assertEquals(OffsetDateTime.parse("1960-01-01T23:03:20+02:00"), javaTime.getOffset());
        OffsetDateTime inserted = OffsetDateTime.parse("1960-01-01T23:03:20+02:00");
        if (persistenceUnitName.contains("postgres")) {
          // postgres stores in UTC
          OffsetDateTime inUtc = inserted.withOffsetSameInstant(ZoneOffset.UTC);
          assertEquals(inUtc, javaTime.getOffset());
        } else {
          assertEquals(inserted, javaTime.getOffset());
        }
        return null;
       });

      // insert a new entity into the database
      BigInteger newId = new BigInteger("2");
      ZonedDateTime newZoned = ZonedDateTime.now();
      OffsetDateTime newOffset = OffsetDateTime.now();

      this.template.execute((s) -> {
        JavaTime42WithZone toInsert = new JavaTime42WithZone();
        toInsert.setId(newId);
//        toInsert.setZoned(newZoned);
        toInsert.setOffset(newOffset);
        entityManager.persist(toInsert);
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute((s) -> {
        JavaTime42WithZone readBack = entityManager.find(JavaTime42WithZone.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
//        assertEquals(newZoned, readBack.getZoned());
        assertEquals(newOffset, readBack.getOffset());
        entityManager.remove(readBack);
        return null;
      });
    } finally {
      entityManager.close();
      // EntityManagerFactory should be closed by spring.
    }
  }

}
