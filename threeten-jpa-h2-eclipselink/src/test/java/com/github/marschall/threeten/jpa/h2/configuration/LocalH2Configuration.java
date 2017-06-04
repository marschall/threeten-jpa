package com.github.marschall.threeten.jpa.h2.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import com.github.marschall.threeten.jpa.test.configuration.H2Configuration;
import com.github.marschall.threeten.jpa.test.configuration.TransactionManagerConfiguration;

@Configuration
@Import({
  H2Configuration.class,
  TransactionManagerConfiguration.class
})
public class LocalH2Configuration {

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPersistenceUnitName("threeten-jpa-eclipselink-h2");
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
