package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * Type for {@link ZonedDateTime}.
 */
public class Jdbc42ZonedDateTimeType extends AbstractThreeTenTimestampWithTimeZoneType {

  /**
   * Singleton access.
   */
  public static final UserType INSTANCE = new Jdbc42ZonedDateTimeType();

  /**
   * Name of the type.
   */
  public static final String NAME = "Jdbc42ZonedDateTimeType";

  @Override
  public Class<?> returnedClass() {
    return ZonedDateTime.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
    return rs.getObject(names[0], ZonedDateTime.class);
  }

}
