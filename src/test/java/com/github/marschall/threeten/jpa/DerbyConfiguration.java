package com.github.marschall.threeten.jpa;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DerbyConfiguration {
  
  private static final AtomicInteger COUNT = new AtomicInteger();
  
  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
        .setName("Derby-" + COUNT.incrementAndGet())
        .setType(DERBY)
        .addScript("schema.sql")
        .addScript("data.sql")
        .build();
  }
  @Bean
  public PlatformTransactionManager txManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

}
