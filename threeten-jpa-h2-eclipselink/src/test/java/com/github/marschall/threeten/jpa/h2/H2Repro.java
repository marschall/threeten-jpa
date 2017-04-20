package com.github.marschall.threeten.jpa.h2;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;

import org.h2.api.TimestampWithTimeZone;

public class H2Repro {

  public static void main(String[] args) throws SQLException {
    try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:");
         Statement statement = connection.createStatement()) {
      statement.execute("CREATE TABLE TIMESTAMP_TZ_COMPARE ("
              + "  ID BIGINT NOT NULL,"
              + "  OFFSET_TIME TIMESTAMP WITH TIME ZONE,"
              + "  PRIMARY KEY (ID)"
              + ")");
      statement.execute("INSERT INTO TIMESTAMP_TZ_COMPARE ("
              + "    ID,"
              + "    OFFSET_TIME)"
              + "VALUES("
              + "    1,"
              + "    '1960-01-01 23:03:20.66+02:00')");
      statement.execute("INSERT INTO TIMESTAMP_TZ_COMPARE ("
              + "    ID,"
              + "    OFFSET_TIME)"
              + "VALUES("
              + "    2,"
              + "    '1999-01-23 08:26:56.66-03:30')");

      try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID"
              + " FROM TIMESTAMP_TZ_COMPARE"
              + " WHERE (OFFSET_TIME < ?)")) {

        OffsetDateTime offsetDateTime = OffsetDateTime.parse("1998-01-31T09:26:56.66+02:00");
//        TimestampWithTimeZone dbValue = new H2OffsetDateTimeConverter().convertToDatabaseColumn(offsetDateTime);
        TimestampWithTimeZone dbValue = new TimestampWithTimeZone(1023039L, 34016660000000L, (short) 120);
        preparedStatement.setObject(1, dbValue);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          while (resultSet.next()) {
            System.out.println(resultSet.getBigDecimal("ID"));
          }
        }
      }
    }

  }

}
