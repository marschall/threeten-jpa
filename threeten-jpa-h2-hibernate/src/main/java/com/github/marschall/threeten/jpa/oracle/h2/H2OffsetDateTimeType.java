package com.github.marschall.threeten.jpa.oracle.h2;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.Objects;

import org.h2.api.TimestampWithTimeZone;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;


/**
 * Type for {@link OffsetDateTime}.
 */
public class H2OffsetDateTimeType implements UserType {

  /**
   * Singleton access
   */
  public static final H2OffsetDateTimeType INSTANCE = new H2OffsetDateTimeType();

  /**
   * Name of the type.
   */
  public static final String NAME = "H2OffsetDateTimeType";

  // OffsetDateTime <-> TimestampWithTimeZone specific method

  // unfortunately H2 maps TimestampWithTimeZone to Types.OTHER
  private static final int[] SQL_TYPES = new int[] {Types.OTHER};

  @Override
  public Class<?> returnedClass() {
    return OffsetDateTime.class;
  }

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
    // only 1.4.193 and later support getObject(String, Class)
    TimestampWithTimeZone timestampWithTimeZone = (TimestampWithTimeZone) rs.getObject(names[0]);
    if (timestampWithTimeZone != null) {
      return TimestampWithTimeZoneConverter.timestampWithTimeZoneToOffsetDateTime(timestampWithTimeZone);
    } else {
      return null;
    }
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
    if (value != null) {
      st.setObject(index, value);
    } else {
      st.setNull(index, SQL_TYPES[0]);
    }
  }

  // general JSR-310 methods

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

}
