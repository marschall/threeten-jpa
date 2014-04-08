package com.github.marschall.threeten.jpa;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

public class HibernateH2Configuration {

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPersistenceUnitName("threeten-jpa-hibernate-h2");
    bean.setJpaDialect(new HibernateJpaDialect());
    bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    bean.setDataSource(dataSource);
    return bean;
  }

}
