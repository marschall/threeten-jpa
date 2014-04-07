package com.github.marschall.threeten.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class HibernateConfiguration {
  

  @Bean
  public LocalEntityManagerFactoryBean entityManager() {
    LocalEntityManagerFactoryBean bean = new LocalEntityManagerFactoryBean();
    bean.setPersistenceUnitName("threeten-jpa-hibernate");
    bean.setJpaDialect(new HibernateJpaDialect());
    bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    return bean;
  }

}
