package com.github.marschall.threeten.jpa;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@Ignore("Open JPA does not yet support JPA 2.1")
@ContextConfiguration(classes = {DerbyConfiguration.class, OpenJpaConfiguration.class})
public class OpenJpaConveterTest extends AbstractTransactionalJUnit4SpringContextTests {
  
  @PersistenceContext
  private EntityManager entityManager;
  
  @Test
  public void load() {
    Query query = this.entityManager.createQuery("SELECT t FROM JavaTime t");
    List<?> resultList = query.getResultList();
    assertThat(resultList, hasSize(1));
  }

}
