package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.time.ZonedDateTime;

import oracle.sql.TIMESTAMPTZ;

/**
 * Type for {@link ZonedDateTime}.
 */
public class OracleZonedDateTimeType extends AbstractTimestamptzType {

  /**
   * Singleton access
   */
  public static final OracleZonedDateTimeType INSTANCE = new OracleZonedDateTimeType();

  @Override
  public Class<?> returnedClass() {
    return ZonedDateTime.class;
  }

  @Override
  TIMESTAMPTZ convertFromThreeTen(Object value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  Object convertToThreeTen(TIMESTAMPTZ timestamptz) {
    // TODO Auto-generated method stub
    return null;
  }

}
