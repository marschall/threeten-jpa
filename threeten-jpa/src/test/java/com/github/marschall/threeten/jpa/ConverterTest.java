package com.github.marschall.threeten.jpa;

import static com.github.marschall.threeten.jpa.Constants.PERSISTENCE_UNIT_NAME;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import com.github.marschall.threeten.jpa.test.Travis;
import com.github.marschall.threeten.jpa.test.configuration.DerbyConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.FirebirdConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.H2Configuration;
import com.github.marschall.threeten.jpa.test.configuration.HsqlConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.MariaDbConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.MysqlConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.PostgresConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.SqlServerConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.TransactionManagerConfiguration;

public class ConverterTest {

  /**
   * java.sql.Time seems to be converted to seconds always
   */
  private static final TemporalUnit TIME_RESOLUTION = ChronoUnit.SECONDS;

  private AnnotationConfigApplicationContext applicationContext;
  private TransactionOperations template;

  public static List<Arguments> parameters() {
    List<Arguments> parameters = new ArrayList<>();
    parameters.add(Arguments.of(DerbyConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-derby", ChronoUnit.NANOS));
    parameters.add(Arguments.of(H2Configuration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-h2", ChronoUnit.NANOS));
    parameters.add(Arguments.of(HsqlConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-hsql", ChronoUnit.NANOS));
    parameters.add(Arguments.of(MysqlConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-mysql", ChronoUnit.MICROS));
    parameters.add(Arguments.of(PostgresConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-postgres", ChronoUnit.MICROS));
    if (!Travis.isTravis()) {
      parameters.add(Arguments.of(FirebirdConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-firebird", ChronoUnit.MILLIS));
      parameters.add(Arguments.of(MariaDbConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-mariadb", ChronoUnit.MICROS));
      parameters.add(Arguments.of(SqlServerConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-sqlserver", ChronoUnit.MICROS));
    }

    parameters.add(Arguments.of(DerbyConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-derby", ChronoUnit.NANOS));
    parameters.add(Arguments.of(H2Configuration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-h2", ChronoUnit.NANOS));
    parameters.add(Arguments.of(HsqlConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-hsql", ChronoUnit.NANOS));
    parameters.add(Arguments.of(MysqlConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-mysql", ChronoUnit.MICROS));
    if (!Travis.isTravis()) {
      // for whatever reason the Hibernate tests don't see the table in the script
//      parameters.add(Arguments.of(FirebirdConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-firebird", ChronoUnit.MILLIS));
      parameters.add(Arguments.of(MariaDbConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-mariadb", ChronoUnit.MICROS));
      parameters.add(Arguments.of(SqlServerConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-sqlserver", ChronoUnit.MICROS));
    }
    parameters.add(Arguments.of(PostgresConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-postgres", ChronoUnit.MICROS));
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
        TypedQuery<JavaTime> query = entityManager.createQuery("SELECT t FROM JavaTime t ORDER BY t.id ASC", JavaTime.class);
        List<JavaTime> resultList = query.getResultList();
        assertThat(resultList, hasSize(2));

        // validate the entity inserted by SQL
        JavaTime javaTime = resultList.get(0);
        assertEquals(LocalTime.parse("15:09:02"), javaTime.getLocalTime());
        assertEquals(LocalDate.parse("1988-12-25"), javaTime.getLocalDate());
        assertEquals(LocalDateTime.parse("1980-01-01T23:03:20.123456789").truncatedTo(resolution), javaTime.getLocalDateTime());
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
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);

      // insert a new entity into the database
      BigInteger newId = BigInteger.valueOf(3L);
      LocalTime newTime = LocalTime.now().withNano(123_456_789).truncatedTo(TIME_RESOLUTION);
      LocalDate newDate = LocalDate.now();
      LocalDateTime newDateTime = LocalDateTime.now().withNano(123_456_789).truncatedTo(resolution);

      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTime toInsert = new JavaTime();
        toInsert.setId(newId);
        toInsert.setLocalDate(newDate);
        toInsert.setLocalTime(newTime);
        toInsert.setLocalDateTime(newDateTime);
        entityManager.persist(toInsert);
        status.flush();
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTime readBack = entityManager.find(JavaTime.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newTime, readBack.getLocalTime());
        assertEquals(newDate, readBack.getLocalDate());
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
