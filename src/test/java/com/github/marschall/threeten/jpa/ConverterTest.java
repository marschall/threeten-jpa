package com.github.marschall.threeten.jpa;

import static com.github.marschall.threeten.jpa.Constants.PERSISTENCE_UNIT_NAME;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(Parameterized.class)
public class ConverterTest {

  private final Class<?> datasourceConfiguration;
  private final Class<?> jpaConfiguration;
  private final String persistenceUnitName;
  private AnnotationConfigApplicationContext applicationContext;

  public ConverterTest(Class<?> datasourceConfiguration, Class<?> jpaConfiguration, String persistenceUnitName) {
    this.datasourceConfiguration = datasourceConfiguration;
    this.jpaConfiguration = jpaConfiguration;
    this.persistenceUnitName = persistenceUnitName;
  }
  
  @Parameters(name = "{2}")
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
        new Object[]{DerbyConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-derby"},
        new Object[]{H2Configuration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-h2"},
        new Object[]{HsqlConfiguration.class, EclipseLinkConfiguration.class, "threeten-jpa-eclipselink-hsql"},
        new Object[]{DerbyConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-derby"},
        new Object[]{H2Configuration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-h2"},
        new Object[]{HsqlConfiguration.class, HibernateConfiguration.class, "threeten-jpa-hibernate-hsql"}
        );
  }
  
  @Before
  public void setUp() {
    this.applicationContext = new AnnotationConfigApplicationContext();
    this.applicationContext.register(this.datasourceConfiguration, TransactionManagerConfiguration.class, this.jpaConfiguration);
    ConfigurableEnvironment environment = this.applicationContext.getEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map<String, Object> source = singletonMap(PERSISTENCE_UNIT_NAME, this.persistenceUnitName);
    propertySources.addFirst(new MapPropertySource("persistence unit name", source));
    this.applicationContext.refresh();
  }
  
  @After
  public void tearDown() {
    this.applicationContext.close();
  }
  
  @Test
  public void runTest() {
    PlatformTransactionManager txManager = this.applicationContext.getBean(PlatformTransactionManager.class);
    TransactionTemplate template = new TransactionTemplate(txManager);
    template.execute((s) -> runInTransation());
  }
  
  private Void runInTransation() {
    EntityManagerFactory factory = this.applicationContext.getBean(EntityManagerFactory.class);
    EntityManager entityManager = factory.createEntityManager();
    try {

      Query query = entityManager.createQuery("SELECT t FROM JavaTime t");
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
      entityManager.persist(toInsert);

      JavaTime readBack = entityManager.find(JavaTime.class, new BigInteger("2"));
      assertNotNull(readBack);
    } finally {
      entityManager.close();
    }
    return null;
  }

}
