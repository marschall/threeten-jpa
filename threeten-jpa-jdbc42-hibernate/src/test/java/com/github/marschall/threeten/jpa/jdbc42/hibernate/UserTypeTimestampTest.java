package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import static com.github.marschall.threeten.jpa.test.Travis.isTravis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionOperations;

import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalH2Configuration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalHsqlConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalMariaDbConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalMysqlConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalOracleConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalPostgresConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalSqlServerConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.entity.JavaTimestamp42;
import com.github.marschall.threeten.jpa.test.HundredNanoseconds;

public class UserTypeTimestampTest {

  private AnnotationConfigApplicationContext applicationContext;
  private TransactionOperations template;

  public static List<Arguments> parameters() {
    List<Arguments> parameters = new ArrayList<>();
    parameters.add(Arguments.of(LocalHsqlConfiguration.class, "threeten-jpa-hibernate-hsql", ChronoUnit.NANOS));
    parameters.add(Arguments.of(LocalMysqlConfiguration.class, "threeten-jpa-hibernate-mysql", ChronoUnit.MICROS));
    if (!isTravis()) {
      // for whatever reason the firebird scripts can not see the table
      //    parameters.add(Arguments.of(LocalFirebirdConfiguration.class, "threeten-jpa-hibernate-firebird", ChronoUnit.MILLIS));
      parameters.add(Arguments.of(LocalMariaDbConfiguration.class, "threeten-jpa-hibernate-mariadb", ChronoUnit.MICROS));
      parameters.add(Arguments.of(LocalSqlServerConfiguration.class, "threeten-jpa-hibernate-sqlserver", HundredNanoseconds.INSTANCE));
      parameters.add(Arguments.of(LocalOracleConfiguration.class, "threeten-jpa-hibernate-oracle", ChronoUnit.NANOS));
    }
    parameters.add(Arguments.of(LocalH2Configuration.class, "threeten-jpa-hibernate-h2", ChronoUnit.NANOS));
    parameters.add(Arguments.of(LocalPostgresConfiguration.class, "threeten-jpa-hibernate-postgres", ChronoUnit.MICROS));

    // incomplete JDBC 4.2 support
    // parameters.add(Arguments.of(LocalDerbyConfiguration.class, "threeten-jpa-hibernate-derby", ChronoUnit.NANOS));
    return parameters;
  }

  private void setUp(Class<?> jpaConfiguration, String persistenceUnitName) {
    this.applicationContext = new AnnotationConfigApplicationContext(jpaConfiguration);

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

  @ParameterizedTest
  @MethodSource("parameters")
  public void read(Class<?> jpaConfiguration, String persistenceUnitName, TemporalUnit resolution) {
    assumeFalse(persistenceUnitName.endsWith("-mariadb"));
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {

      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        TypedQuery<JavaTimestamp42> query = entityManager.createQuery("SELECT t FROM JavaTimestamp42 t ORDER BY t.id ASC", JavaTimestamp42.class);
        List<JavaTimestamp42> resultList = query.getResultList();
        assertThat(resultList, hasSize(2));

        // validate the entity inserted by SQL
        JavaTimestamp42 javaTimestamp = resultList.get(1);
        assertEquals(LocalDateTime.parse("2016-03-27T02:55:00.123456789").truncatedTo(resolution), javaTimestamp.getLocalDateTime());
        return null;
      });

    } finally {
      this.tearDown();
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void readAndWrite(Class<?> jpaConfiguration, String persistenceUnitName, TemporalUnit resolution) {
    assumeFalse(persistenceUnitName.endsWith("-mysql"));
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {

      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);

      // insert a new entity into the database
      BigInteger newId = BigInteger.valueOf(3L);
      LocalDateTime newLocalDateTime = LocalDateTime.now().withNano(123_456_789).truncatedTo(resolution);

      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTimestamp42 toInsert = new JavaTimestamp42();
        toInsert.setId(newId);
        toInsert.setLocalDateTime(newLocalDateTime);
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
        assertEquals(newLocalDateTime, readBack.getLocalDateTime());
        entityManager.remove(readBack);
        status.flush();
        return null;
      });
    } finally {
      this.tearDown();
    }
  }

}
