package com.github.marschall.threeten.jpa.mssql.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.github.marschall.threeten.jpa.mssql.impl.DateTimeOffsetConverter;
import com.microsoft.sqlserver.jdbc.ISQLServerPreparedStatement;
import com.microsoft.sqlserver.jdbc.ISQLServerResultSet;

import microsoft.sql.DateTimeOffset;

/**
 * Type mapping {@link OffsetDateTime} to {@code DateTimeOffset}.
 */
public class MssqlOffsetDateTimeType implements UserType {

  /**
   * Singleton access.
   */
  public static final UserType INSTANCE = new MssqlOffsetDateTimeType();

  /**
   * Name of the type.
   */
  public static final String NAME = "MssqlOffsetDateTimeType";

  private static final int[] SQL_TYPES = new int []{Types.TIMESTAMP_WITH_TIMEZONE};

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Class<?> returnedClass() {
    return OffsetDateTime.class;
  }

  @Override
  public boolean equals(Object x, Object y) {
    return Objects.equals(x, y);
  }

  @Override
  public int hashCode(Object x) {
    return Objects.hashCode(x);
  }

  @Override
  public Object deepCopy(Object value) {
    return value;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(Object value) {
    return (Serializable) value;
  }

  @Override
  public Object assemble(Serializable cached, Object owner) {
    return cached;
  }

  @Override
  public Object replace(Object original, Object target, Object owner) {
    return original;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
    DateTimeOffset dateTimeOffset = rs.unwrap(ISQLServerResultSet.class).getDateTimeOffset(names[0]);
    return DateTimeOffsetConverter.dateTimeOffsetToOffsetDateTime(dateTimeOffset);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
    DateTimeOffset dateTimeOffset = DateTimeOffsetConverter.offsetDateTimeToDateTimeOffset((OffsetDateTime) value);
    st.unwrap(ISQLServerPreparedStatement.class).setDateTimeOffset(index, dateTimeOffset);
  }

}
