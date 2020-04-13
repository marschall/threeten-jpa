package com.github.marschall.threeten.jpa.test.configuration;


import static com.github.marschall.threeten.jpa.test.Travis.isTravis;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class PostgresConfiguration {

  @Bean
  public DataSource dataSource() {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setSuppressClose(true);
    String userName = System.getProperty("user.name");
    // defaults from Postgres.app
    dataSource.setUrl("jdbc:postgresql:" + userName);
    dataSource.setUsername(userName);
    String password = isTravis() ? "" : "Cent-Quick-Space-Bath-8";
    dataSource.setPassword(password);
    return dataSource;
  }

  @Bean
  public DatabasePopulator databasePopulator() {
    return new ResourceDatabasePopulator(
        new ClassPathResource("postgres-schema.sql"),
        new ClassPathResource("postgres-data.sql"));
  }

}
