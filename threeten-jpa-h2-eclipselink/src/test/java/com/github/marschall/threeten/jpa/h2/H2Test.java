package com.github.marschall.threeten.jpa.h2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.github.marschall.threeten.jpa.h2.configuration.H2Configuration;

@ContextConfiguration(classes = H2Configuration.class)
public class H2Test extends AbstractTransactionalJUnit4SpringContextTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  public void readFirstRow() {
    JavaTime42WithZone firstRow = this.entityManager.find(JavaTime42WithZone.class, new BigInteger("1"));

    ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(2, 0);
    OffsetDateTime expectedOffset = OffsetDateTime.of(1960, 1, 1, 23, 3, 20, 660000000, zoneOffset);
    assertEquals(expectedOffset, firstRow.getOffsetDateTime());
  }

  @Test
  public void readSecondRow() {
    JavaTime42WithZone secondRow = this.entityManager.find(JavaTime42WithZone.class, new BigInteger("2"));
    ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(-3, -30);
    OffsetDateTime expectedOffset = OffsetDateTime.of(1999, 1, 23, 8, 26, 56, 660000000, zoneOffset);
    assertEquals(expectedOffset, secondRow.getOffsetDateTime());
  }

  @Test
  @Ignore("H2 Bug")
  public void criteriaApi() {
    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<JavaTime42WithZone> query = builder.createQuery(JavaTime42WithZone.class);
    OffsetDateTime offsetDateTime = OffsetDateTime.parse("1998-01-31T09:26:56.66+02:00");
    Root<JavaTime42WithZone> root = query.from(JavaTime42WithZone.class);
    CriteriaQuery<JavaTime42WithZone> beforeTwelfeFive = query.where(
            builder.lessThan(root.get(JavaTime42WithZone_.offsetDateTime), offsetDateTime));
    JavaTime42WithZone entity = this.entityManager.createQuery(beforeTwelfeFive).getSingleResult();
    assertNotNull(entity);

    assertEquals(new BigInteger("1"), entity.getId());

    ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(2, 0);
    OffsetDateTime expectedOffset = OffsetDateTime.of(1960, 1, 1, 23, 3, 20, 660000000, zoneOffset);
    assertEquals(expectedOffset, entity.getOffsetDateTime());

  }

}
