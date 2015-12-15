package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.time.OffsetDateTime;

import com.github.marschall.threeten.jpa.oracle.impl.TimestamptzConverter;

import oracle.sql.TIMESTAMPTZ;

/**
 * Type for {@link OffsetDateTime}.
 */
public class OracleOffsetDateTimeType extends AbstractTimestamptzType {

  /**
   * Singleton access
   */
  public static final OracleOffsetDateTimeType INSTANCE = new OracleOffsetDateTimeType();

  /**
   * Name of the type.
   */
  public static final String NAME = "OracleOffsetDateTimeType";

  @Override
  public Class<?> returnedClass() {
    return OffsetDateTime.class;
  }

  @Override
  Object convertToThreeTen(TIMESTAMPTZ timestamptz) {
    return TimestamptzConverter.timestamptzToOffsetDateTime(timestamptz);
  }

  @Override
  TIMESTAMPTZ convertFromThreeTen(Object value) {
    return TimestamptzConverter.offsetDateTimeToTimestamptz((OffsetDateTime) value);
  }

}
