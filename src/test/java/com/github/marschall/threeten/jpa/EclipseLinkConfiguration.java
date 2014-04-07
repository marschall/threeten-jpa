package com.github.marschall.threeten.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

@Configuration
public class EclipseLinkConfiguration {
  
  @Bean
  public LocalEntityManagerFactoryBean entityManager() {
    LocalEntityManagerFactoryBean bean = new LocalEntityManagerFactoryBean();
    bean.setPersistenceUnitName("threeten-jpa-eclipselink");
    bean.setJpaDialect(new EclipseLinkJpaDialect());
    bean.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
    return bean;
  }

}
