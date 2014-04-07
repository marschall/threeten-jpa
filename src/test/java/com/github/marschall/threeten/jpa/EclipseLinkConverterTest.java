package com.github.marschall.threeten.jpa;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(classes = {DerbyConfiguration.class, EclipseLinkConfiguration.class})
public class EclipseLinkConverterTest extends AbstractTransactionalJUnit4SpringContextTests {
  
  @PersistenceContext
  private EntityManager entityManager;
  
  @Test
  public void load() {
    Query query = this.entityManager.createQuery("SELECT t FROM JavaTime t");
    List<?> resultList = query.getResultList();
    assertThat(resultList, hasSize(1));
  }

}
