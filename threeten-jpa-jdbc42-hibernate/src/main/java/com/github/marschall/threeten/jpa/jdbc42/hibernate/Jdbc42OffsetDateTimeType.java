package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import org.hibernate.engine.spi.SessionImplementor;

/**
 * Type for {@link OffsetDateTime}.
 */
public class Jdbc42OffsetDateTimeType extends AbstraceThreeTenTimestampWithTimeZoneType {

  /**
   * Singleton access
   */
  public static final Jdbc42OffsetDateTimeType INSTANCE = new Jdbc42OffsetDateTimeType();

  @Override
  public Class<?> returnedClass() {
    return OffsetDateTime.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
    return rs.getObject(names[0], OffsetDateTime.class);
  }

}