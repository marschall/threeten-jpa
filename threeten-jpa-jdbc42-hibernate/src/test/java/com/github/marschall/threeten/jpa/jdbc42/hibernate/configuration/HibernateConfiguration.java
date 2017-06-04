package com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

@Configuration
public class HibernateConfiguration {

  @Bean
  public JpaDialect jpaDialect() {
    return new HibernateJpaDialect();
  }

}
