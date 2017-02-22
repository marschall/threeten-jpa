package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.time.OffsetDateTime;

import org.hibernate.usertype.UserType;

import com.github.marschall.threeten.jpa.oracle.impl.TimestamptzConverter;

import oracle.sql.TIMESTAMPTZ;

/**
 * Type mapping {@link OffsetDateTime} to {@code TIMESTAMPTZ}.
 */
public class OracleOffsetDateTimeType extends AbstractTimestamptzType {

  /**
   * Singleton access.
   */
  public static final UserType INSTANCE = new OracleOffsetDateTimeType();

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
