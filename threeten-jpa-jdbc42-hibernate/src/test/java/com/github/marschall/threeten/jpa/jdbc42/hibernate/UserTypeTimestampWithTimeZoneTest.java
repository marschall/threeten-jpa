package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import static com.github.marschall.threeten.jpa.test.Travis.isTravis;
import static java.time.temporal.ChronoUnit.MICROS;
import static java.time.temporal.ChronoUnit.NANOS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalOracleConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalPostgresConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.LocalSqlServerConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.entity.JavaTime42WithZone;
import com.github.marschall.threeten.jpa.test.HundredNanoseconds;

public class UserTypeTimestampWithTimeZoneTest {

  private AnnotationConfigApplicationContext applicationContext;
  private TransactionOperations template;

  public static List<Arguments> parameters() {

    List<Arguments> parameters = new ArrayList<>();
    parameters.add(Arguments.of(LocalHsqlConfiguration.class, "threeten-jpa-hibernate-hsql", NANOS));

    if (!isTravis()) {
      parameters.add(Arguments.of(LocalSqlServerConfiguration.class, "threeten-jpa-hibernate-sqlserver", HundredNanoseconds.INSTANCE));
      parameters.add(Arguments.of(LocalOracleConfiguration.class, "threeten-jpa-hibernate-oracle", NANOS));
    }
//    parameters.add(Arguments.of(LocalDerbyConfiguration.class, "threeten-jpa-hibernate-derby", ChronoUnit.NANOS));
    parameters.add(Arguments.of(LocalH2Configuration.class, "threeten-jpa-hibernate-h2", NANOS));
    parameters.add(Arguments.of(LocalPostgresConfiguration.class, "threeten-jpa-hibernate-postgres", MICROS));

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

  private void tearDown() {
    this.applicationContext.close();
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void read(Class<?> jpaConfiguration, String persistenceUnitName, TemporalUnit resolution) {
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // read the entity inserted by SQL
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        TypedQuery<JavaTime42WithZone> query = entityManager.createQuery(
                "SELECT t FROM JavaTime42WithZone t ORDER BY t.id ASC", JavaTime42WithZone.class);
        List<JavaTime42WithZone> resultList = query.getResultList();
        assertThat(resultList, hasSize(2));

        // validate the entity inserted by SQL
        JavaTime42WithZone javaTime = resultList.get(0);
        OffsetDateTime inserted = OffsetDateTime.parse("1960-01-01T23:03:20.123456789+02:30").truncatedTo(resolution);
        if (jpaConfiguration.getName().contains("Postgres")) {
          // Postgres stores in UTC
          inserted = inserted.withOffsetSameInstant(ZoneOffset.UTC);
          assertEquals(inserted, javaTime.getOffset());
        } else {
          assertEquals(inserted, javaTime.getOffset());
        }

        javaTime = resultList.get(1);
        inserted = OffsetDateTime.parse("1999-01-23T08:26:56.123456789-05:30").truncatedTo(resolution);
        if (jpaConfiguration.getName().contains("Postgres")) {
          // Postgres stores in UTC
          inserted = inserted.withOffsetSameInstant(ZoneOffset.UTC);
          assertEquals(inserted, javaTime.getOffset());
        } else {
          assertEquals(inserted, javaTime.getOffset());
        }
        return null;
      });
    } finally {
      this.tearDown();
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void write(Class<?> jpaConfiguration, String persistenceUnitName, TemporalUnit resolution) {
    this.setUp(jpaConfiguration, persistenceUnitName);
    try {
      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
      // insert a new entity into the database
      BigInteger newId = BigInteger.valueOf(3L);

      OffsetDateTime now;
      if (persistenceUnitName.endsWith("-postgres")) {
        // PostgreS only supports UTC
        now = OffsetDateTime.now(ZoneOffset.UTC);
      } else {
        now = OffsetDateTime.now();
      }
      OffsetDateTime newOffset = now.withNano(123_456_789).truncatedTo(resolution);

      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTime42WithZone toInsert = new JavaTime42WithZone();
        toInsert.setId(newId);
        toInsert.setOffset(newOffset);
        entityManager.persist(toInsert);
        status.flush();
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        JavaTime42WithZone readBack = entityManager.find(JavaTime42WithZone.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newOffset, readBack.getOffset());
        entityManager.remove(readBack);
        status.flush();
        return null;
      });
    } finally {
      this.tearDown();
    }
  }

}
