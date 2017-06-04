package com.github.marschall.threeten.jpa.test.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class SqlServerConfiguration {

  @Bean
  public DataSource dataSource() {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setSuppressClose(true);
    // defaults from Postgres.app
//    dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=your_password");
    dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=master");
    dataSource.setUsername("sa");
    dataSource.setPassword("Cent-Quick-Space-Bath-8");
    return dataSource;
  }

  @Bean
  public DatabasePopulator databasePopulator() {
    return new ResourceDatabasePopulator(
        new ClassPathResource("sqlserver-schema.sql"),
        new ClassPathResource("sqlserver-data.sql"));
  }

}
