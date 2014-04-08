package com.github.marschall.threeten.jpa;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.Test;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

public abstract class AbstractConverterTest extends AbstractTransactionalJUnit4SpringContextTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  public void load() {
    Query query = this.entityManager.createQuery("SELECT t FROM JavaTime t");
    List<?> resultList = query.getResultList();
    assertThat(resultList, hasSize(1));
    
    JavaTime javaTime = (JavaTime) resultList.get(0);
    assertEquals(LocalDate.parse("1988-12-25"), javaTime.getLocalDate());
    assertEquals(LocalTime.parse("15:09:02"), javaTime.getLocalTime());
    assertEquals(LocalDateTime.parse("1960-01-01T23:03:20"), javaTime.getLocalDateTime());
    
    JavaTime toInsert = new JavaTime();
    toInsert.setId(new BigInteger("2"));
    toInsert.setLocalDate(LocalDate.now());
    toInsert.setLocalTime(LocalTime.now());
    toInsert.setLocalDateTime(LocalDateTime.now());
    this.entityManager.persist(toInsert);
    
    JavaTime readBack = this.entityManager.find(JavaTime.class, new BigInteger("2"));
    assertNotNull(readBack);
  }

}