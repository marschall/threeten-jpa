package com.github.marschall.threeten.jpa.test.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class Db2Configuration {

  @Bean
  public DataSource dataSource() {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setSuppressClose(true);
    dataSource.setUrl("jdbc:db2://localhost:50000/jdbc");
    dataSource.setUsername("db2inst1");
    dataSource.setPassword("Cent-Quick-Space-Bath-8");
    return dataSource;
  }

  @Bean
  public DatabasePopulator databasePopulator() {
    return new ResourceDatabasePopulator(
        new ClassPathResource("db2-schema.sql"),
        new ClassPathResource("db2-data.sql"));
  }

}
