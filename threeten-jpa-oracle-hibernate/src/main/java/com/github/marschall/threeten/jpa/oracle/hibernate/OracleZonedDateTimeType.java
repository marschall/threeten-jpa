package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.time.ZonedDateTime;

import com.github.marschall.threeten.jpa.oracle.impl.TimestamptzConverter;

import oracle.sql.TIMESTAMPTZ;

/**
 * Type for {@link ZonedDateTime}.
 */
public class OracleZonedDateTimeType extends AbstractTimestamptzType {

  /**
   * Singleton access
   */
  public static final OracleZonedDateTimeType INSTANCE = new OracleZonedDateTimeType();

  /**
   * Name of the type.
   */
  public static final String NAME = "OracleZonedDateTimeType";

  @Override
  public Class<?> returnedClass() {
    return ZonedDateTime.class;
  }

  @Override
  Object convertToThreeTen(TIMESTAMPTZ timestamptz) {
    return TimestamptzConverter.timestamptzToZonedDateTime(timestamptz);
  }

  @Override
  TIMESTAMPTZ convertFromThreeTen(Object value) {
    return TimestamptzConverter.zonedDateTimeToTimestamptz((ZonedDateTime) value);
  }

}
