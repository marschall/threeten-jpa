package com.github.marschall.threeten.jpa.test.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class FirebirdConfiguration {


  @Bean
  public DataSource dataSource() {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setSuppressClose(true);
    // https://www.firebirdsql.org/file/documentation/drivers_documentation/java/faq.html#jdbc-urls-java.sql.drivermanager
    // https://github.com/FirebirdSQL/jaybird/wiki/Jaybird-and-Firebird-3
    dataSource.setUrl("jdbc:firebirdsql://localhost:3050/jdbc?charSet=utf-8");
    // https://github.com/almeida/docker-firebird
    dataSource.setUsername("jdbc");
    dataSource.setPassword("Cent-Quick-Space-Bath-8");
    return dataSource;
  }

  @Bean
  public DatabasePopulator databasePopulator() {
    ResourceDatabasePopulator schemaPopulator = new ResourceDatabasePopulator(
            new ClassPathResource("firebird-schema.sql"));
    schemaPopulator.setSeparator("!!");

    DatabasePopulator dataPopulator = new ResourceDatabasePopulator(
            new ClassPathResource("firebird-data.sql"));
    return new CompositeDatabasePopulator(schemaPopulator, dataPopulator);
  }

}
