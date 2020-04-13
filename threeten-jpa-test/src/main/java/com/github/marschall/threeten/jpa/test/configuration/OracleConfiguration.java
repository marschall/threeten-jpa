package com.github.marschall.threeten.jpa.test.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class OracleConfiguration {

  @Bean
  public DataSource dataSource() {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setSuppressClose(true);
    dataSource.setUrl("jdbc:oracle:thin:@localhost:1521/ORCLPDB1");
    dataSource.setUsername("jdbc");
    dataSource.setPassword("Cent-Quick-Space-Bath-8");
    Properties connectionProperties = new Properties();
    connectionProperties.setProperty("oracle.net.disableOob", "true");
    dataSource.setConnectionProperties(connectionProperties);
    return dataSource;
  }

  @Bean
  public DatabasePopulator databasePopulator() {

    ResourceDatabasePopulator schemaPopulator = new ResourceDatabasePopulator(
            new ClassPathResource("oracle-schema.sql"));
    schemaPopulator.setSeparator("!!");

    DatabasePopulator dataPopulator = new ResourceDatabasePopulator(
            new ClassPathResource("oracle-data.sql"));

    return new CompositeDatabasePopulator(schemaPopulator, dataPopulator);
  }

}
