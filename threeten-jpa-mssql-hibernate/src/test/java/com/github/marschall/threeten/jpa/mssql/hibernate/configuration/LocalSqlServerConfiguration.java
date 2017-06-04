package com.github.marschall.threeten.jpa.mssql.hibernate.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.github.marschall.threeten.jpa.test.configuration.SqlServerConfiguration;
import com.github.marschall.threeten.jpa.test.configuration.TransactionManagerConfiguration;

@Configuration
@Import({
  SqlServerConfiguration.class,
  TransactionManagerConfiguration.class
})
public class LocalSqlServerConfiguration {

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPersistenceUnitName("threeten-jpa-hibernate-sqlserver");
    bean.setJpaDialect(jpaDialect());
    bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    bean.setDataSource(dataSource);
    return bean;
  }

  @Bean
  public JpaDialect jpaDialect() {
    return new HibernateJpaDialect();
  }

}
