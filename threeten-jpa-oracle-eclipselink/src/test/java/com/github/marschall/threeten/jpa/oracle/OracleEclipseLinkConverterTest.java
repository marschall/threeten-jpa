package com.github.marschall.threeten.jpa.oracle;

import static com.github.marschall.threeten.jpa.oracle.Constants.PERSISTENCE_UNIT_NAME;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.support.TransactionOperations;

@Disabled("needs oracle database")
public class OracleEclipseLinkConverterTest extends AbstractTransactionalJUnit4SpringContextTests {

  private TransactionOperations template;

  private AnnotationConfigApplicationContext applicationContext;

  private void setUp(String persistenceUnitName) {
    this.applicationContext = new AnnotationConfigApplicationContext();
    this.applicationContext.register(LocalOracleConfiguration.class);
    ConfigurableEnvironment environment = this.applicationContext.getEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map<String, Object> source = singletonMap(PERSISTENCE_UNIT_NAME, persistenceUnitName);
    propertySources.addFirst(new MapPropertySource("persistence unit name", source));
    this.applicationContext.refresh();

    this.template = this.applicationContext.getBean(TransactionOperations.class);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "threeten-jpa-eclipselink-oracle",
      //"threeten-jpa-eclipselink-oracle12",
      "threeten-jpa-eclipselink-oracle12patched"
  })
  public void readFirstRow(String persistenceUnitName) {
    this.setUp(persistenceUnitName);
    EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
    this.template.execute(status -> {
      EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
      OracleJavaTime firstRow = entityManager.find(OracleJavaTime.class, BigInteger.valueOf(1L));

      ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(2, 0);
      OffsetDateTime expectedOffset = OffsetDateTime.of(1997, 1, 31, 9, 26, 56, 660000000, zoneOffset);
      assertEquals(expectedOffset, firstRow.getOffsetDateTime());

      ZoneId zoneId = ZoneId.of("US/Pacific");
      ZonedDateTime expectedZone = ZonedDateTime.of(1999, 1, 15, 8, 0, 0, 0, zoneId);
      assertEquals(expectedZone, firstRow.getZonedDateTime());

      assertNotNull(firstRow.getCalendar());

      assertEquals(Period.of(123, 2, 0), firstRow.getPeriod());
      assertEquals(Duration.parse("P4DT5H12M10.222S"), firstRow.getDuration());
      return null;
    });
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "threeten-jpa-eclipselink-oracle",
      //"threeten-jpa-eclipselink-oracle12",
      "threeten-jpa-eclipselink-oracle12patched"
  })
  public void readSecondRow(String persistenceUnitName) {
    this.setUp(persistenceUnitName);

    EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
    this.template.execute(status -> {
      EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
      OracleJavaTime secondRow = entityManager.find(OracleJavaTime.class, BigInteger.valueOf(2L));
      ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(-3, -30);
      OffsetDateTime expectedOffset = OffsetDateTime.of(1999, 1, 15, 8, 26, 56, 660000000, zoneOffset);
      assertEquals(expectedOffset, secondRow.getOffsetDateTime());

      ZoneId zoneId = ZoneId.of("Europe/Berlin");
      ZonedDateTime expectedZone = ZonedDateTime.of(2016, 9, 22, 17, 0, 0, 0, zoneId);
      assertEquals(expectedZone, secondRow.getZonedDateTime());

      assertNotNull(secondRow.getCalendar());

      assertEquals(Period.of(123, 2, 0), secondRow.getPeriod());
      assertEquals(Duration.parse("P4DT5H12M10.222S"), secondRow.getDuration());
      return null;
    });
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "threeten-jpa-eclipselink-oracle",
      //"threeten-jpa-eclipselink-oracle12",
      "threeten-jpa-eclipselink-oracle12patched"
  })
  public void insert(String persistenceUnitName) {
    this.setUp(persistenceUnitName);
      // insert a new entity into the database
      BigInteger newId = BigInteger.valueOf(3L);
      ZoneOffset offset = ZoneOffset.ofHoursMinutes(2, 0);
      ZoneId zoneId = ZoneId.of("Europe/Berlin");
      LocalDateTime localDateTime = LocalDateTime.of(2014, 4, 27, 22, 24, 30, 0);
      OffsetDateTime newOffsetDateTime = OffsetDateTime.of(localDateTime, offset);
      ZonedDateTime newZonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
      Calendar newCalendar = Calendar.getInstance();
      Period newPeriod = Period.of(123_567_789, 11, 0);
      Duration newDuration = Duration.parse("P321456789DT23H55M10.123456789S");


      EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);

      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        OracleJavaTime toInsert = new OracleJavaTime();
        toInsert.setId(newId);
        toInsert.setOffsetDateTime(newOffsetDateTime);
        toInsert.setZonedDateTime(newZonedDateTime);
        toInsert.setCalendar(newCalendar);
        toInsert.setPeriod(newPeriod);
        toInsert.setDuration(newDuration);
        entityManager.persist(toInsert);
        status.flush();
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
        OracleJavaTime readBack = entityManager.find(OracleJavaTime.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newOffsetDateTime, readBack.getOffsetDateTime());
        assertEquals(newZonedDateTime, readBack.getZonedDateTime());
        assertEquals(newCalendar, readBack.getCalendar());
        assertEquals(newPeriod, readBack.getPeriod());
        assertEquals(newDuration, readBack.getDuration());
        entityManager.remove(readBack);
        status.flush();
        return null;
      });
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "threeten-jpa-eclipselink-oracle",
      //"threeten-jpa-eclipselink-oracle12",
      "threeten-jpa-eclipselink-oracle12patched"
  })
  public void criteriaApi(String persistenceUnitName) {
    this.setUp(persistenceUnitName);
    EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
    this.template.execute(status -> {
      EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
      CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      CriteriaQuery<OracleJavaTime> query = builder.createQuery(OracleJavaTime.class);
      OffsetDateTime offsetDateTime = OffsetDateTime.parse("1998-01-31T09:26:56.66+02:00");
      ZonedDateTime zonedDateTime = ZonedDateTime.parse("1998-12-15T08:00:00-08:00[US/Pacific]");
      Root<OracleJavaTime> root = query.from(OracleJavaTime.class);
      CriteriaQuery<OracleJavaTime> beforeTwelfeFive = query.where(builder.and(
              builder.lessThan(root.get(OracleJavaTime_.offsetDateTime), offsetDateTime),
              builder.greaterThan(root.get(OracleJavaTime_.zonedDateTime), zonedDateTime)));
      OracleJavaTime oracleJavaTime = entityManager.createQuery(beforeTwelfeFive).getSingleResult();
      assertNotNull(oracleJavaTime);

      assertEquals(BigInteger.valueOf(1L), oracleJavaTime.getId());

      ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(2, 0);
      OffsetDateTime expectedOffset = OffsetDateTime.of(1997, 1, 31, 9, 26, 56, 660000000, zoneOffset);
      assertEquals(expectedOffset, oracleJavaTime.getOffsetDateTime());

      ZoneId zoneId = ZoneId.of("US/Pacific");
      ZonedDateTime expectedZone = ZonedDateTime.of(1999, 1, 15, 8, 0, 0, 0, zoneId);
      assertEquals(expectedZone, oracleJavaTime.getZonedDateTime());

      return null;
    });
  }

}
