package com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class H2Configuration {

  private static final AtomicInteger COUNT = new AtomicInteger();

  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
         // the spring test context framework keeps application contexts
         // and thus databases around for the entire VM lifetime
         // so be have to create a unique name here to avoid sharing
         // between application contexts
        .setName("H2-TIMESTAMP-WITH-TIME-ZONE" + COUNT.incrementAndGet())
        .setType(H2)
        .addScript("h2-schema.sql")
        .addScript("h2-data.sql")
        .build();
  }

  @Bean
  public PlatformTransactionManager txManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
    transactionManager.setDataSource(dataSource());
    transactionManager.setJpaDialect(jpaDialect());
    return transactionManager;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPersistenceUnitName("threeten-jpa-hibernate-h2");
    bean.setJpaDialect(jpaDialect());
    bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    bean.setDataSource(dataSource);
    return bean;
  }

  @Bean
  public JpaDialect jpaDialect() {
    return new HibernateJpaDialect();
  }

}
