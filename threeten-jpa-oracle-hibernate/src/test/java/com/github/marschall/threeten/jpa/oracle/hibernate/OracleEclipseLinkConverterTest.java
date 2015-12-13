package com.github.marschall.threeten.jpa.oracle.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
    OracleJavaTime byOffset = this.entityManager.find(OracleJavaTime.class, new BigInteger("1"));
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(2, 0);
    OffsetDateTime expected = OffsetDateTime.of(1997, 1, 31, 9, 26, 56, 660000000, offset);
    assertEquals(expected, byOffset.getOffsetDateTime());

    OracleJavaTime byZone = this.entityManager.find(OracleJavaTime.class, new BigInteger("2"));
    ZoneOffset pacificOffset = ZoneOffset.ofHoursMinutes(8, 0);
    expected = OffsetDateTime.of(1999, 1, 15, 8, 0, 0, 0, pacificOffset);
    assertEquals(expected, byZone.getOffsetDateTime());
  }

  @Test
  public void runTest() {
    // insert a new entity into the database
    BigInteger newId = new BigInteger("3");
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(2, 0);
    OffsetDateTime newOffsetDateTime = OffsetDateTime.of(2014, 4, 27, 22, 24, 30, 0, offset);

    this.template.execute((s) -> {
      OracleJavaTime toInsert = new OracleJavaTime();
      toInsert.setId(newId);
      toInsert.setOffsetDateTime(newOffsetDateTime);
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
      entityManager.remove(readBack);
      return null;
    });
  }

}
