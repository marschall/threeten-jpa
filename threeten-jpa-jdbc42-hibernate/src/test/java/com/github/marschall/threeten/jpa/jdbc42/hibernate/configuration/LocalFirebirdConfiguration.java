package com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.github.marschall.threeten.jpa.test.configuration.FirebirdConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.TransactionManagerConfiguration;

@Configuration
@Import({
  FirebirdConfiguration.class,
  HibernateConfiguration.class,
  TransactionManagerConfiguration.class
})
public class LocalFirebirdConfiguration {

  @Autowired
  private JpaDialect jpaDialect;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPersistenceUnitName("threeten-jpa-hibernate-firebird");
    bean.setJpaDialect(this.jpaDialect);
    bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    bean.setDataSource(dataSource);
    return bean;
  }

}
