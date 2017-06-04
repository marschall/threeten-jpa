package com.github.marschall.threeten.jpa.test.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
public class OracleConfiguration {

  @Bean
  public DataSource dataSource() {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setSuppressClose(true);
    dataSource.setUrl("");
    dataSource.setUsername("scott");
    dataSource.setPassword("tiger");
    return dataSource;
  }

}
