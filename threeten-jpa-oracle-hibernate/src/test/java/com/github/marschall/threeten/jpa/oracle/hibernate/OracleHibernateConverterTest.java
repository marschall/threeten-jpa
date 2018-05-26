package com.github.marschall.threeten.jpa.oracle.hibernate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigInteger;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionOperations;

@Transactional
@SpringJUnitConfig(LocalOracleConfiguration.class)
@Disabled("needs oracle database")
public class OracleHibernateConverterTest {

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @Autowired
  private TransactionOperations template;

  @Test
  public void read() {
    // read the entity inserted by SQL
    this.template.execute(status -> {
      EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(this.entityManagerFactory);
      TypedQuery<JavaTimeWithZone> query = entityManager.createQuery("SELECT t FROM JavaTimeWithZone t", JavaTimeWithZone.class);
      List<JavaTimeWithZone> resultList = query.getResultList();
      assertThat(resultList, hasSize(1));

      // validate the entity inserted by SQL
      JavaTimeWithZone javaTime = resultList.get(0);
      assertEquals(ZonedDateTime.parse("1960-01-01T23:03:20-05:00[America/New_York]"), javaTime.getZoned());
      assertEquals(OffsetDateTime.parse("1960-01-01T23:03:20+02:00"), javaTime.getOffset());

      assertEquals(Period.of(123, 2, 0), javaTime.getPeriod());
      assertEquals(Duration.parse("P4DT5H12M10.222S"), javaTime.getDuration());
      return null;
    });
  }

  @Test
  public void insert() {
      // insert a new entity into the database
      BigInteger newId = BigInteger.valueOf(2L);
      ZonedDateTime newZoned = ZonedDateTime.now().withNano(123_456_789);
      OffsetDateTime newOffset = OffsetDateTime.now().withNano(123_456_789);
      Period newPeriod = Period.of(123_567_789, 11, 0);
      Duration newDuration = Duration.parse("P321456789DT23H55M10.123456789S");

      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(this.entityManagerFactory);
        JavaTimeWithZone toInsert = new JavaTimeWithZone();
        toInsert.setId(newId);
        toInsert.setZoned(newZoned);
        toInsert.setOffset(newOffset);
        toInsert.setPeriod(newPeriod);
        toInsert.setDuration(newDuration);
        entityManager.persist(toInsert);
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute(status -> {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(this.entityManagerFactory);
        JavaTimeWithZone readBack = entityManager.find(JavaTimeWithZone.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newZoned, readBack.getZoned());
        assertEquals(newOffset, readBack.getOffset());
        assertEquals(newPeriod, readBack.getPeriod());
        assertEquals(newDuration, readBack.getDuration());
        entityManager.remove(readBack);
        return null;
      });
  }

}
