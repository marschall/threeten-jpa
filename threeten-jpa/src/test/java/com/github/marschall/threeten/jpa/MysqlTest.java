package com.github.marschall.threeten.jpa;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

import com.github.marschall.threeten.jpa.MysqlTest.LocalConfiguration;
import com.github.marschall.threeten.jpa.configuration.MysqlConfiguration;

@ContextConfiguration(classes = {MysqlConfiguration.class, LocalConfiguration.class})
@Sql("classpath:mysql-schema.sql")
//@Sql("classpath:mysql-data.sql")
@Ignore
public class MysqlTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private DataSource dataSource;

  @Test
  public void getObject() throws SQLException {
    //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
                 "SELECT @@system_time_zone, @@global.time_zone, @@session.time_zone");
         ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        System.out.println(resultSet.getString(1));
        System.out.println(resultSet.getString(2));
        System.out.println(resultSet.getString(3));
      }
      try (Statement statement = connection.createStatement()) {
        statement.execute(
            "INSERT INTO JAVA_TIME (ID, DATE_COLUMN,       TIME_COLUMN,     TIMESTAMP_COLUMN) "
          + "VALUES(                1,  DATE '1988-12-25', TIME '15:09:02', TIMESTAMP '1980-01-01 23:03:20')");
      }
      try (PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO JAVA_TIME (ID, DATE_COLUMN, TIME_COLUMN, TIMESTAMP_COLUMN) "
           + "VALUES(                ?, ?,           ?,           ?)")) {
        statement.setBigDecimal(1, new BigDecimal(2));
        statement.setDate(2, new java.sql.Date(88, 12, 25));
        statement.setTime(3, new java.sql.Time(15, 9, 2));
        statement.setTimestamp(4, new java.sql.Timestamp(80, 1, 1, 23, 3, 20, 0));
        statement.executeUpdate();
      }
      try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM JAVA_TIME");
           ResultSet resultSet2 = statement.executeQuery()) {
        while (resultSet2.next()) {
          System.out.println(resultSet2.getLong("ID"));
          System.out.println(resultSet2.getTime("TIME_COLUMN"));
//          System.out.println(resultSet2.getTime("DATE_COLUMN"));
//          System.out.println(resultSet2.getTime("TIMESTAMP_COLUMN"));
        }
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
