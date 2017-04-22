package com.github.marschall.threeten.jpa.mssql.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

public class MssqlConfiguration {

  @Bean
  public DataSource dataSource() {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setSuppressClose(true);
    // defaults from Postgres.app
//    dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=your_password");
    dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=master");
    dataSource.setUsername("sa");
    dataSource.setPassword("Cent-Quick-Space-Bath-8");
    return dataSource;
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
    bean.setPersistenceUnitName("threeten-jpa-eclipselink-mssql");
    bean.setJpaDialect(jpaDialect());
    bean.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
    bean.setDataSource(dataSource);
    return bean;
  }

  @Bean
  public JpaDialect jpaDialect() {
    return new EclipseLinkJpaDialect();
  }

}
