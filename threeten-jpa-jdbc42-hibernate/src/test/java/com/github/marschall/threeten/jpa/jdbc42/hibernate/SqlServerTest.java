package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.marschall.threeten.jpa.jdbc42.hibernate.SqlServerTest.LocalConfiguration;
import com.github.marschall.threeten.jpa.jdbc42.hibernate.configuration.SqlServerConfiguration;

@Ignore
@ContextConfiguration(classes = {SqlServerConfiguration.class, LocalConfiguration.class})
@Sql("classpath:sqlserver-schema.sql")
@Sql("classpath:sqlserver-data.sql")
public class SqlServerTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private DataSource dataSource;

  @Test
  public void getObject() throws SQLException {
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
                 "SELECT OFFSET_TIME"
              + " FROM java_time_42_with_zone");
         ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        Object dateTimeOffset = resultSet.getObject(1);
        assertEquals("microsoft.sql.DateTimeOffset", dateTimeOffset.getClass().getName());
        System.out.println(dateTimeOffset);
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
