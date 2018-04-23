package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalH2Configuration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalHsqlConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalMysqlConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalPostgresConfiguration;

public class UserTypeWithoutTimeZoneTest {

  private AnnotationConfigApplicationContext applicationContext;
  private TransactionTemplate template;

  public static List<Arguments> parameters() {
    List<Arguments> parameters = new ArrayList<>();
    parameters.add(Arguments.of(LocalHsqlConfiguration.class, "threeten-jpa-hibernate-hsql", ChronoUnit.NANOS));
    parameters.add(Arguments.of(LocalMysqlConfiguration.class, "threeten-jpa-hibernate-mysql", ChronoUnit.MICROS));
    parameters.add(Arguments.of(LocalH2Configuration.class, "threeten-jpa-hibernate-h2", ChronoUnit.NANOS));
    // parameters.add(Arguments.of(LocalDerbyConfiguration.class, "threeten-jpa-hibernate-derby", ChronoUnit.NANOS));
    // parameters.add(Arguments.of(LocalSqlServerConfiguration.class, "threeten-jpa-hibernate-sqlserver", ChronoUnit.MICROS));
    parameters.add(Arguments.of(LocalPostgresConfiguration.class, "threeten-jpa-hibernate-postgres", ChronoUnit.MICROS));
    return parameters;
  }

  private void setUp(Class<?> jpaConfiguration, String persistenceUnitName) {
    this.applicationContext = new AnnotationConfigApplicationContext(jpaConfiguration);

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

  private void tearDown() {
    this.applicationContext.close();
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void runTest(Class<?> jpaConfiguration, String persistenceUnitName, ChronoUnit resolution) {
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {

      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        TypedQuery<JavaTime42> query = entityManager.createQuery("SELECT t FROM JavaTime42 t ORDER BY t.id ASC", JavaTime42.class);
        List<JavaTime42> resultList = query.getResultList();
        assertThat(resultList, hasSize(2));

        // validate the entity inserted by SQL
        JavaTime42 javaTime = resultList.get(1);
        assertEquals(LocalTime.parse("02:55:00.123456789").truncatedTo(resolution), javaTime.getLocalTime());
        assertEquals(LocalDate.parse("2016-03-27"), javaTime.getLocalDate());
        assertEquals(LocalDateTime.parse("2016-03-27T02:55:00.123456789").truncatedTo(resolution), javaTime.getLocalDateTime());
        return null;
      });

      // insert a new entity into the database
      BigInteger newId = new BigInteger("3");
      LocalTime newLocalTime = LocalTime.now().withNano(123_456_789).truncatedTo(resolution);
      LocalDate newLocalDate = LocalDate.now();
      LocalDateTime newLocalDateTime = LocalDateTime.now().withNano(123_456_789).truncatedTo(resolution);

      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTime42 toInsert = new JavaTime42();
        toInsert.setId(newId);
        toInsert.setLocalTime(newLocalTime);
        toInsert.setLocalDate(newLocalDate);
        toInsert.setLocalDateTime(newLocalDateTime);
        entityManager.persist(toInsert);
        status.flush();
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTime42 readBack = entityManager.find(JavaTime42.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newLocalTime, readBack.getLocalTime());
        assertEquals(newLocalDate, readBack.getLocalDate());
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
