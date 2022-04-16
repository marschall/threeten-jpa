package com.github.marschall.threeten.jpa;

import static com.github.marschall.threeten.jpa.Constants.PERSISTENCE_UNIT_NAME;
import static com.github.marschall.threeten.jpa.test.Travis.isTravis;
import static java.time.temporal.ChronoUnit.MICROS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.NANOS;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
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
import com.github.marschall.threeten.jpa.entity.JavaTimestamp42;
import com.github.marschall.threeten.jpa.test.HundredNanoseconds;
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

public class ConverterTimestampTest {

  private AnnotationConfigApplicationContext applicationContext;
  private TransactionOperations template;

  public static List<Arguments> parameters() {
    List<Arguments> parameters = new ArrayList<>();
    parameters.add(Arguments.of(DerbyConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-derby", NANOS));
    parameters.add(Arguments.of(H2Configuration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-h2", NANOS));
    parameters.add(Arguments.of(HsqlConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-hsql", NANOS));
    parameters.add(Arguments.of(MysqlConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-mysql", MICROS));
    parameters.add(Arguments.of(PostgresConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-postgres", MICROS));
    if (!isTravis()) {
      parameters.add(Arguments.of(FirebirdConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-firebird", MILLIS));
      parameters.add(Arguments.of(MariaDbConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-mariadb", MICROS));
      parameters.add(Arguments.of(SqlServerConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-sqlserver", HundredNanoseconds.INSTANCE));
      parameters.add(Arguments.of(OracleConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-oracle", NANOS));
    }

    parameters.add(Arguments.of(DerbyConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-derby", NANOS));
    parameters.add(Arguments.of(H2Configuration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-h2", NANOS));
    parameters.add(Arguments.of(HsqlConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-hsql", NANOS));
    parameters.add(Arguments.of(MysqlConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-mysql", MICROS));
    if (!isTravis()) {
      // for whatever reason the Hibernate tests don't see the table in the script
//      parameters.add(Arguments.of(FirebirdConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-firebird", ChronoUnit.MILLIS));
      parameters.add(Arguments.of(MariaDbConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-mariadb", MICROS));
      parameters.add(Arguments.of(SqlServerConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-sqlserver", HundredNanoseconds.INSTANCE));
      parameters.add(Arguments.of(OracleConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-oracle", NANOS));
    }
    parameters.add(Arguments.of(PostgresConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-postgres", MICROS));
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
  public void read(Class<?> datasourceConfiguration, Class<?> jpaConfiguration, String persistenceUnitName, TemporalUnit resolution) {
    this.setUp(datasourceConfiguration, jpaConfiguration, persistenceUnitName);
    try {
      this.mysqlHack(persistenceUnitName);
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        TypedQuery<JavaTimestamp42> query = entityManager.createQuery("SELECT t FROM JavaTimestamp42 t ORDER BY t.id ASC", JavaTimestamp42.class);
        List<JavaTimestamp42> resultList = query.getResultList();
        assertThat(resultList, hasSize(2));

        // validate the entity inserted by SQL
        JavaTimestamp42 javaTimestamp = resultList.get(0);
        assertEquals(LocalDateTime.parse("1980-01-01T23:03:20.123456789").truncatedTo(resolution), javaTimestamp.getLocalDateTime());
        return null;
      });

    } finally {
      this.tearDown();
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void readAndWrite(Class<?> datasourceConfiguration, Class<?> jpaConfiguration, String persistenceUnitName, TemporalUnit resolution) {
    this.setUp(datasourceConfiguration, jpaConfiguration, persistenceUnitName);
    try {
      this.mysqlHack(persistenceUnitName);
      if (persistenceUnitName.endsWith("-hsql")) {
        // hsql is weird, it looks like reading has nanos but writing has millis
        resolution = MILLIS;
      }
      if (persistenceUnitName.endsWith("-mariadb")) {
        // mariadb is weird, it looks like reading has micros but writing has millis
        resolution = MILLIS;
      }
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);

      // insert a new entity into the database
      BigInteger newId = BigInteger.valueOf(3L);
      LocalDateTime newDateTime = LocalDateTime.now().withNano(123_456_789).truncatedTo(resolution);

      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTimestamp42 toInsert = new JavaTimestamp42();
        toInsert.setId(newId);
        toInsert.setLocalDateTime(newDateTime);
        entityManager.persist(toInsert);
        status.flush();
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTimestamp42 readBack = entityManager.find(JavaTimestamp42.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newDateTime, readBack.getLocalDateTime());
        entityManager.remove(readBack);
        status.flush();
        return null;
      });
    } finally {
      this.tearDown();
    }
  }

}
