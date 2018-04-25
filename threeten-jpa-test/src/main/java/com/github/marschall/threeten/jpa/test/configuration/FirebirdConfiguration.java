package com.github.marschall.threeten.jpa.test.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

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

}
