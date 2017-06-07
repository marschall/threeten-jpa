package com.github.marschall.threeten.jpa.zoned.hibernate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.marschall.threeten.jpa.zoned.hibernate.configuration.LocalH2Configuration;
import com.github.marschall.threeten.jpa.zoned.hibernate.configuration.LocalHsqlConfiguration;
import com.github.marschall.threeten.jpa.zoned.hibernate.configuration.LocalPostgresConfiguration;

@RunWith(Parameterized.class)
public class ZonedDateTimeTypeTest {

  private final Class<?> jpaConfiguration;
  private AnnotationConfigApplicationContext applicationContext;
  private TransactionTemplate template;

  public ZonedDateTimeTypeTest(Class<?> jpaConfiguration, String persistenceUnitName) {
    this.jpaConfiguration = jpaConfiguration;
  }

  @Parameters(name = "{1}")
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
            new Object[]{LocalHsqlConfiguration.class, "threeten-jpa-hibernate-hsql"},
//            new Object[]{LocalSqlServerConfiguration.class, "threeten-jpa-hibernate-sqlserver"},
            new Object[]{LocalH2Configuration.class, "threeten-jpa-hibernate-h2"},
            new Object[]{LocalPostgresConfiguration.class, "threeten-jpa-hibernate-postgres"}
            );
  }

  @Before
  public void setUp() {
    this.applicationContext = new AnnotationConfigApplicationContext();
    this.applicationContext.register(this.jpaConfiguration);
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
  public void read() {
    EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
    EntityManager entityManager = factory.createEntityManager();
    try {
      // read the entity inserted by SQL
      this.template.execute(status -> {
        Query query = entityManager.createQuery("SELECT t FROM JavaTime42Zoned t ORDER BY t.id ASC");
        List<?> resultList = query.getResultList();
        assertThat(resultList, hasSize(1));

        // validate the entity inserted by SQL
        JavaTime42Zoned javaTime = (JavaTime42Zoned) resultList.get(0);
        ZonedDateTime inserted = ZonedDateTime.parse("1999-01-23T03:26:56.123456789-05:00[America/New_York]");
        if (jpaConfiguration.getName().contains("Postgres")) {
          inserted = ZonedDateTime.parse("1999-01-23T03:26:56.123457000-05:00[America/New_York]");
        } else if (jpaConfiguration.getName().contains("Hsql")) {
          inserted = ZonedDateTime.parse("1999-01-23T03:26:56.123456000-05:00[America/New_York]");
        }
        assertEquals(inserted, javaTime.getZonedDateTime());
        return null;
      });
    } finally {
      entityManager.close();
      // EntityManagerFactory should be closed by spring.
    }
  }

  @Test
  public void write() {
    EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
    EntityManager entityManager = factory.createEntityManager();
    try {
      // insert a new entity into the database
      BigInteger newId = new BigInteger("3");
      ZonedDateTime newZoned = ZonedDateTime.now();

      this.template.execute(status -> {
        JavaTime42Zoned toInsert = new JavaTime42Zoned();
        toInsert.setId(newId);
        toInsert.setZonedDateTime(newZoned);
        entityManager.persist(toInsert);
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute(status -> {
        JavaTime42Zoned readBack = entityManager.find(JavaTime42Zoned.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newZoned, readBack.getZonedDateTime());
        entityManager.remove(readBack);
        return null;
      });
    } finally {
      entityManager.close();
      // EntityManagerFactory should be closed by spring.
    }
  }
}
