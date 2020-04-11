package com.github.marschall.threeten.jpa;

import static com.github.marschall.threeten.jpa.Constants.PERSISTENCE_UNIT_NAME;
import static com.github.marschall.threeten.jpa.test.Travis.isTravis;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionOperations;

import com.github.marschall.threeten.jpa.configuration.EclipseLinkConfiguration;
import com.github.marschall.threeten.jpa.configuration.HibernateConfiguration;
import com.github.marschall.threeten.jpa.entity.JavaDate42;
import com.github.marschall.threeten.jpa.test.configuration.DerbyConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.FirebirdConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.H2Configuration;
import com.github.marschall.threeten.jpa.test.configuration.HsqlConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.MariaDbConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.MysqlConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.OracleConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.PostgresConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.SqlServerConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.TransactionManagerConfiguration;

public class ConverterDateTest {

  private AnnotationConfigApplicationContext applicationContext;
  private TransactionOperations template;

  public static List<Arguments> parameters() {
    List<Arguments> parameters = new ArrayList<>();
    parameters.add(Arguments.of(DerbyConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-derby"));
    parameters.add(Arguments.of(H2Configuration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-h2"));
    parameters.add(Arguments.of(HsqlConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-hsql"));
    parameters.add(Arguments.of(MysqlConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-mysql"));
    parameters.add(Arguments.of(PostgresConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-postgres"));
    if (!isTravis()) {
      parameters.add(Arguments.of(FirebirdConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-firebird"));
      parameters.add(Arguments.of(MariaDbConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-mariadb"));
      parameters.add(Arguments.of(SqlServerConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-sqlserver"));
      parameters.add(Arguments.of(OracleConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-oracle"));
    }

    parameters.add(Arguments.of(DerbyConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-derby"));
    parameters.add(Arguments.of(H2Configuration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-h2"));
    parameters.add(Arguments.of(HsqlConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-hsql"));
    parameters.add(Arguments.of(MysqlConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-mysql"));
    if (!isTravis()) {
      // for whatever reason the Hibernate tests don't see the table in the script
//      parameters.add(Arguments.of(FirebirdConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-firebird"));
      parameters.add(Arguments.of(MariaDbConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-mariadb"));
      parameters.add(Arguments.of(SqlServerConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-sqlserver"));
      parameters.add(Arguments.of(OracleConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-oracle"));
    }
    parameters.add(Arguments.of(PostgresConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-postgres"));
    return parameters;
  }

  private void setUp(Class<?> datasourceConfiguration, Class<?> jpaConfiguration, String persistenceUnitName) {
    this.applicationContext = new AnnotationConfigApplicationContext();
    this.applicationContext.register(datasourceConfiguration, TransactionManagerConfiguration.class, jpaConfiguration);
    ConfigurableEnvironment environment = this.applicationContext.getEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map<String, Object> source = singletonMap(PERSISTENCE_UNIT_NAME, persistenceUnitName);
    propertySources.addFirst(new MapPropertySource("persistence unit name", source));
    this.applicationContext.refresh();

    this.template = this.applicationContext.getBean(TransactionOperations.class);

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

  private void tearDown() {
    this.applicationContext.close();
  }

  private void mysqlHack(String persistenceUnitName) {
    // http://stackoverflow.com/questions/43560374/mysql-client-vs-server-time-zone
    if (persistenceUnitName.contains("mysql")) {
      DataSource dataSource = this.applicationContext.getBean(DataSource.class);
      JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);
      String serverTimeZone = jdbcOperations.queryForObject("SELECT @@system_time_zone", String.class);
      String systemTimeZone = ZoneId.systemDefault().getId();
      if (systemTimeZone.equals("Etc/UTC")) {
        systemTimeZone = "UTC";
      }
      assumeTrue(serverTimeZone.equals(systemTimeZone));
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void read(Class<?> datasourceConfiguration, Class<?> jpaConfiguration, String persistenceUnitName) {
    this.setUp(datasourceConfiguration, jpaConfiguration, persistenceUnitName);
    try {
      this.mysqlHack(persistenceUnitName);
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        TypedQuery<JavaDate42> query = entityManager.createQuery("SELECT t FROM JavaDate42 t ORDER BY t.id ASC", JavaDate42.class);
        List<JavaDate42> resultList = query.getResultList();
        assertThat(resultList, hasSize(2));

        // validate the entity inserted by SQL
        JavaDate42 javaDate = resultList.get(0);
        assertEquals(LocalDate.parse("1988-12-25"), javaDate.getLocalDate());
        return null;
      });

    } finally {
      this.tearDown();
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void readAndWrite(Class<?> datasourceConfiguration, Class<?> jpaConfiguration, String persistenceUnitName) {
    this.setUp(datasourceConfiguration, jpaConfiguration, persistenceUnitName);
    try {
      this.mysqlHack(persistenceUnitName);
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);

      // insert a new entity into the database
      BigInteger newId = BigInteger.valueOf(3L);
      LocalDate newDate = LocalDate.now();

      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaDate42 toInsert = new JavaDate42();
        toInsert.setId(newId);
        toInsert.setLocalDate(newDate);
        entityManager.persist(toInsert);
        status.flush();
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaDate42 readBack = entityManager.find(JavaDate42.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newDate, readBack.getLocalDate());
        entityManager.remove(readBack);
        status.flush();
        return null;
      });
    } finally {
      this.tearDown();
    }
  }

}
