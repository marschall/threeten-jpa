package com.github.marschall.threeten.jpa.test.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TransactionManagerConfiguration {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private JpaDialect jpaDialect;

  @Bean
  public PlatformTransactionManager txManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
    transactionManager.setDataSource(this.dataSource);
    transactionManager.setJpaDialect(this.jpaDialect);
    return transactionManager;
  }

  @Bean
  public TransactionOperations transactionOperations(PlatformTransactionManager txManager) {
    TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    return new TransactionTemplate(txManager, transactionDefinition);
  }

}
