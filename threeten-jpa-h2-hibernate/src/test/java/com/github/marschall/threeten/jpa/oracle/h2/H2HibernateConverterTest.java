package com.github.marschall.threeten.jpa.oracle.h2;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@ContextConfiguration(classes = H2Configuration.class)
public class H2HibernateConverterTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private PlatformTransactionManager txManager;

  @PersistenceContext
  private EntityManager entityManager;

  private TransactionTemplate template;

  @Before
  public void setUp() {
    this.template = new TransactionTemplate(txManager);
  }

  @Ignore("bug in H2")
  @Test
  public void read() {
    // read the entity inserted by SQL
    this.template.execute((s) -> {
      Query query = entityManager.createQuery("SELECT t FROM JavaTimeWithZone t");
      List<?> resultList = query.getResultList();
      assertThat(resultList, hasSize(1));

      // validate the entity inserted by SQL
      JavaTimeWithZone javaTime = (JavaTimeWithZone) resultList.get(0);
      assertEquals(OffsetDateTime.parse("1960-01-01T23:03:20+02:00"), javaTime.getOffset());
      return null;
    });
  }

  @Test
  public void runTest() {
    try {

      // insert a new entity into the database
      BigInteger newId = new BigInteger("2");
      OffsetDateTime newOffset = OffsetDateTime.now();

      this.template.execute((s) -> {
        JavaTimeWithZone toInsert = new JavaTimeWithZone();
        toInsert.setId(newId);
        toInsert.setOffset(newOffset);
        entityManager.persist(toInsert);
        // the transaction should trigger a flush and write to the database
        return null;
      });

      // validate the new entity inserted into the database
      this.template.execute((s) -> {
        JavaTimeWithZone readBack = entityManager.find(JavaTimeWithZone.class, newId);
        assertNotNull(readBack);
        assertEquals(newId, readBack.getId());
        assertEquals(newOffset, readBack.getOffset());
        entityManager.remove(readBack);
        return null;
      });
    } finally {
      entityManager.close();
      // EntityManagerFactory should be closed by spring.
    }
  }

}
