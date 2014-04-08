package com.github.marschall.threeten.jpa;

import static com.github.marschall.threeten.jpa.Constants.PERSISTENCE_UNIT_NAME;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

@RunWith(Parameterized.class)
@Ignore
public class ConveterTest {
  
  private final Class<?> jpaClass;
  private final Class<?> databaseClass;
  private final String persistenceUnitName;
  private AnnotationConfigApplicationContext applicationContext;

  public ConveterTest(Class<?> databaseClass, Class<?> jpaClass, String persistenceUnitName) {
    this.databaseClass = databaseClass;
    this.jpaClass = jpaClass;
    this.persistenceUnitName = persistenceUnitName;
  }
  
  @Before
  public void setUp() {
    applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(this.databaseClass, this.jpaClass);
    MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
    Map<String, Object> map = Collections.singletonMap(PERSISTENCE_UNIT_NAME, this.persistenceUnitName);
    propertySources.addFirst(new MapPropertySource("persistenceUnitName", map));
    applicationContext.refresh();
  }
  
  @Test
  public void select() {
    EntityManager entityManager = this.applicationContext.getBean(EntityManager.class);
    
    Query query = entityManager.createQuery("SELECT t FROM JavaTime t");
    List<?> resultList = query.getResultList();
    assertThat(resultList, hasSize(1));
    
    JavaTime javaTime = (JavaTime) resultList.get(0);
  }
  
  @After
  public void tearDown() {
    applicationContext.close();
  }
  
}
