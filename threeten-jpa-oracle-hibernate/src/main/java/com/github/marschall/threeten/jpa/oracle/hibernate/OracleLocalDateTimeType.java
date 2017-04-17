package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.github.marschall.threeten.jpa.oracle.impl.TimestamptzConverter;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.sql.TIMESTAMP;

/**
 * Type mapping from {@link LocalDateTime} to {@code TIMESTAMP}.
 *
 * @deprecated use ojdbc8 12.2.0.1 and Jdbc42LocalDateTimeType from threeten-jpa-jdbc42-hibernate
 */
@Deprecated
public class OracleLocalDateTimeType extends AbstractThreeTenType {

  /**
   * Singleton access.
   */
  public static final UserType INSTANCE = new OracleLocalDateTimeType();

  /**
   * Name of the type.
   */
  public static final String NAME = "OracleLocalDateTimeType";

  private static final int[] SQL_TYPES = new int []{Types.TIMESTAMP};

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Class<?> returnedClass() {
    return LocalDateTime.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
    OracleResultSet oracleResultSet = rs.unwrap(OracleResultSet.class);
    TIMESTAMP timestamp = oracleResultSet.getTIMESTAMP(names[0]);
    if (timestamp == null) {
      return null;
    } else {
      return TimestamptzConverter.timestampToLocalDateTime(timestamp);
    }
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
    OraclePreparedStatement oraclePreparedStatement = st.unwrap(OraclePreparedStatement.class);
    if (value == null) {
      oraclePreparedStatement.setTIMESTAMP(index, null);
    } else {
      TIMESTAMP timestamp = TimestamptzConverter.localDateTimeToTimestamp((LocalDateTime) value);
      oraclePreparedStatement.setTIMESTAMP(index, timestamp);
    }
  }

}
