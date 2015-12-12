package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

import org.hibernate.engine.spi.SessionImplementor;

/**
 * Type for {@link ZonedDateTime}.
 */
public class Jdbc42ZonedDateTimeType extends AbstraceThreeTenTimestampWithTimeZoneType {

  /**
   * Singleton access
   */
  public static final Jdbc42ZonedDateTimeType INSTANCE = new Jdbc42ZonedDateTimeType();

  @Override
  public Class<?> returnedClass() {
    return ZonedDateTime.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
    return rs.getObject(names[0], ZonedDateTime.class);
  }

}
