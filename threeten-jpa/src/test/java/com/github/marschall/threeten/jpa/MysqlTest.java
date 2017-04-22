package com.github.marschall.threeten.jpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.marschall.threeten.jpa.MysqlTest.LocalConfiguration;
import com.github.marschall.threeten.jpa.configuration.MysqlConfiguration;

@ContextConfiguration(classes = {MysqlConfiguration.class, LocalConfiguration.class})
@Sql("classpath:mysql-schema.sql")
@Sql("classpath:mysql-data.sql")
public class MysqlTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private DataSource dataSource;

  @Test
  public void getObject() throws SQLException {
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
                 "SELECT @@system_time_zone, @@global.time_zone, @@session.time_zone");
         ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        System.out.println(resultSet.getString(1));
        System.out.println(resultSet.getString(2));
        System.out.println(resultSet.getString(3));
      }
    }
  }

  @Configuration
  static class LocalConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager txManager() {
      return new DataSourceTransactionManager(this.dataSource);
    }

  }

}
