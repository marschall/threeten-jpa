package com.github.marschall.threeten.jpa;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

@Configuration
public class EclipseLinkDerbyConfiguration {
  
//  @Autowired
//  private Environment environment;
  
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPersistenceUnitName("threeten-jpa-eclipselink-derby");
//    bean.setPersistenceUnitName(environment.getProperty(PERSISTENCE_UNIT_NAME));
    bean.setJpaDialect(new EclipseLinkJpaDialect());
    bean.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
    bean.setDataSource(dataSource);
    return bean;
  }

}
