package com.github.marschall.threeten.jpa;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.OpenJpaDialect;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;

@Configuration
public class OpenJpaConfiguration {
  
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPersistenceUnitName("threeten-jpa-openjpa");
    bean.setJpaDialect(new OpenJpaDialect());
    bean.setJpaVendorAdapter(new OpenJpaVendorAdapter());
    bean.setDataSource(dataSource);
    return bean;
  }

}
