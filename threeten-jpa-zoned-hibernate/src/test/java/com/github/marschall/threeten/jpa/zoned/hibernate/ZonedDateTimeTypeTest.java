package com.github.marschall.threeten.jpa.zoned.hibernate;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionOperations;

import com.github.marschall.threeten.jpa.zoned.hibernate.configuration.LocalH2Configuration;
import com.github.marschall.threeten.jpa.zoned.hibernate.configuration.LocalHsqlConfiguration;
import com.github.marschall.threeten.jpa.zoned.hibernate.configuration.LocalPostgresConfiguration;

public class ZonedDateTimeTypeTest {

  private AnnotationConfigApplicationContext applicationContext;
  private TransactionOperations template;

  public static List<Arguments> parameters() {
    List<Arguments> parameters = new ArrayList<>();
    parameters.add(Arguments.of(LocalHsqlConfiguration.class, "threeten-jpa-hibernate-hsql", ChronoUnit.NANOS));
//    if (!Travis.isTravis()) {
//      parameters.add(Arguments.of(LocalSqlServerConfiguration.class, "threeten-jpa-hibernate-sqlserver", ChronoUnit.MICROS));
//    }
    parameters.add(Arguments.of(LocalH2Configuration.class, "threeten-jpa-hibernate-h2", ChronoUnit.NANOS));
    parameters.add(Arguments.of(LocalPostgresConfiguration.class, "threeten-jpa-hibernate-postgres", ChronoUnit.MICROS));
    return parameters;
  }

  private void setUp(Class<?> jpaConfiguration, String persistenceUnitName) {
    this.applicationContext = new AnnotationConfigApplicationContext();
    this.applicationContext.register(jpaConfiguration);
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

  private void tearDown(Class<?> jpaConfiguration, String persistenceUnitName) {
    this.applicationContext.close();
  }

  private ZonedDateTime getInsertedValue(ChronoUnit resolution) {
    return ZonedDateTime.parse("1999-01-23T03:26:56.123456789-05:00[America/New_York]").truncatedTo(resolution);
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void read(Class<?> jpaConfiguration, String persistenceUnitName, ChronoUnit resolution) {
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        TypedQuery<JavaTime42Zoned> query = entityManager.createQuery(
                "SELECT t FROM JavaTime42Zoned t ORDER BY t.zonedDateTime ASC", JavaTime42Zoned.class);
        List<JavaTime42Zoned> resultList = query.getResultList();
        assertThat(resultList, hasSize(1));

        // validate the entity inserted by SQL
        JavaTime42Zoned javaTime = resultList.get(0);
        ZonedDateTime inserted = this.getInsertedValue(resolution);
        assertEquals(inserted, javaTime.getZonedDateTime());
        return null;
      });
    } finally {
      this.tearDown(jpaConfiguration, persistenceUnitName);
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void readJpqlLessThan(Class<?> jpaConfiguration, String persistenceUnitName, ChronoUnit resolution) {
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        ZonedDateTime inserted = this.getInsertedValue(resolution);
        ZonedDateTime earlier = inserted.withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                .minusHours(1L);
        TypedQuery<JavaTime42Zoned> query = entityManager.createQuery(
                "SELECT t FROM JavaTime42Zoned t WHERE t.zonedDateTime.timestamp_utc < :value", JavaTime42Zoned.class);
        // https://hibernate.atlassian.net/browse/HHH-7302
        query.setParameter("value", earlier.toOffsetDateTime());
        List<JavaTime42Zoned> result = query.getResultList();

        assertThat(result, empty());

        return null;
      });
    } finally {
      this.tearDown(jpaConfiguration, persistenceUnitName);
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  @Disabled("HHH-7302")
  public void readCriteriaApiLessThan(Class<?> jpaConfiguration, String persistenceUnitName, ChronoUnit resolution) {
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        ZonedDateTime inserted = this.getInsertedValue(resolution);
        ZonedDateTime earlier = inserted.withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                .minusHours(1L);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JavaTime42Zoned> query = builder.createQuery(JavaTime42Zoned.class);
        Root<JavaTime42Zoned> root = query.from(JavaTime42Zoned.class);
        CriteriaQuery<JavaTime42Zoned> beforeTwelfeFive = query.where(
                builder.lessThan(root.get(JavaTime42Zoned_.zonedDateTime), earlier));

        List<JavaTime42Zoned> result = entityManager.createQuery(beforeTwelfeFive).getResultList();

        assertThat(result, empty());

        return null;
      });
    } finally {
      this.tearDown(jpaConfiguration, persistenceUnitName);
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void readNativeLessThan(Class<?> jpaConfiguration, String persistenceUnitName, ChronoUnit resolution) {
    // https://hibernate.atlassian.net/browse/HHH-7302
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        ZonedDateTime inserted = this.getInsertedValue(resolution);
        ZonedDateTime earlier = inserted.withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                .minusHours(1L);
        Query query = entityManager.createNativeQuery("SELECT * FROM JAVA_TIME_42_ZONED t WHERE t.timestamp_utc < ?1",
                JavaTime42Zoned.class);
        query.setParameter(1, earlier.toOffsetDateTime().atZoneSameInstant(ZoneOffset.UTC));
        List<?> result = query.getResultList();

        assertThat(result, empty());

        return null;
      });
    } finally {
      this.tearDown(jpaConfiguration, persistenceUnitName);
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void readJpqlEqual(Class<?> jpaConfiguration, String persistenceUnitName, ChronoUnit resolution) {
    // https://hibernate.atlassian.net/browse/HHH-7302
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        ZonedDateTime inserted = this.getInsertedValue(resolution);
        TypedQuery<JavaTime42Zoned> query = entityManager.createQuery(
                "SELECT t FROM JavaTime42Zoned t WHERE t.zonedDateTime = :value", JavaTime42Zoned.class);
        query.setParameter("value", inserted);
        List<JavaTime42Zoned> result = query.getResultList();

        assertThat(result, hasSize(1));
        assertEquals(inserted, result.get(0).getZonedDateTime());

        return null;
      });
    } finally {
      this.tearDown(jpaConfiguration, persistenceUnitName);
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void orderJpql(Class<?> jpaConfiguration, String persistenceUnitName, ChronoUnit resolution) {
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {
      // https://hibernate.atlassian.net/browse/HHH-7302
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        TypedQuery<JavaTime42Zoned> query = entityManager.createQuery(
                "SELECT t FROM JavaTime42Zoned t ORDER BY t.zonedDateTime.timestamp_utc", JavaTime42Zoned.class);
        List<JavaTime42Zoned> result = query.getResultList();

        assertThat(result, hasSize(1));

        return null;
      });
    } finally {
      this.tearDown(jpaConfiguration, persistenceUnitName);
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void write(Class<?> jpaConfiguration, String persistenceUnitName, ChronoUnit resolution) {
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // insert a new entity into the database
      BigInteger newId = BigInteger.valueOf(3L);
      ZonedDateTime newZoned = ZonedDateTime.now()
              .withNano(123_456_789)
              .truncatedTo(resolution);

      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTime42Zoned toInsert = new JavaTime42Zoned();
        toInsert.setId(newId);
        toInsert.setZonedDateTime(newZoned);
        entityManager.persist(toInsert);
        status.flush();
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTime42Zoned readBack = entityManager.find(JavaTime42Zoned.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newZoned, readBack.getZonedDateTime());
        entityManager.remove(readBack);
        status.flush();
        return null;
      });
    } finally {
      this.tearDown(jpaConfiguration, persistenceUnitName);
    }
  }
}
