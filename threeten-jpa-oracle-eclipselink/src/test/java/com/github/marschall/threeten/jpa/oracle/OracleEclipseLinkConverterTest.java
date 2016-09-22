package com.github.marschall.threeten.jpa.oracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@ContextConfiguration(classes = OracleConfiguration.class)
@Ignore("needs oracle database")
public class OracleEclipseLinkConverterTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private PlatformTransactionManager txManager;

  @PersistenceContext
  private EntityManager entityManager;

  private TransactionTemplate template;

  @Before
  public void setUp() {
    this.template = new TransactionTemplate(txManager);
  }

  @Test
  public void read() {
    OracleJavaTime firstRow = this.entityManager.find(OracleJavaTime.class, new BigInteger("1"));
    ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(2, 0);
    OffsetDateTime expectedOffset = OffsetDateTime.of(1997, 1, 31, 9, 26, 56, 660000000, zoneOffset);
    assertEquals(expectedOffset, firstRow.getOffsetDateTime());

    ZoneId zoneId = ZoneId.of("US/Pacific");
    ZonedDateTime expectedZone = ZonedDateTime.of(1999, 1, 15, 8, 0, 0, 0, zoneId);
    assertEquals(expectedZone, firstRow.getZonedDateTime());

    OracleJavaTime secondRow = this.entityManager.find(OracleJavaTime.class, new BigInteger("2"));
    zoneOffset = ZoneOffset.ofHoursMinutes(-3, -30);
    expectedOffset = OffsetDateTime.of(1999, 1, 15, 8, 26, 56, 660000000, zoneOffset);
    assertEquals(expectedOffset, secondRow.getOffsetDateTime());

    zoneId = ZoneId.of("Europe/Berlin");
    expectedZone = ZonedDateTime.of(2016, 9, 22, 17, 0, 0, 0, zoneId);
    assertEquals(expectedZone, secondRow.getZonedDateTime());
  }

  @Test
  public void runTest() {
    // insert a new entity into the database
    BigInteger newId = new BigInteger("3");
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(2, 0);
    ZoneId zoneId = ZoneId.of("Europe/Berlin");
    LocalDateTime localDateTime = LocalDateTime.of(2014, 4, 27, 22, 24, 30, 0);
    OffsetDateTime newOffsetDateTime = OffsetDateTime.of(localDateTime, offset);
    ZonedDateTime newZonedDateTime = ZonedDateTime.of(localDateTime, zoneId);

    this.template.execute((s) -> {
      OracleJavaTime toInsert = new OracleJavaTime();
      toInsert.setId(newId);
      toInsert.setOffsetDateTime(newOffsetDateTime);
      toInsert.setZonedDateTime(newZonedDateTime);
      entityManager.persist(toInsert);
      // the transaction should trigger a flush and write to the database
      return null;
    });

    // validate the new entity inserted into the database
    this.template.execute((s) -> {
      OracleJavaTime readBack = entityManager.find(OracleJavaTime.class, newId);
      assertNotNull(readBack);
      assertEquals(newId, readBack.getId());
      assertEquals(newOffsetDateTime, readBack.getOffsetDateTime());
      assertEquals(newZonedDateTime, readBack.getZonedDateTime());
      entityManager.remove(readBack);
      return null;
    });
  }

}
