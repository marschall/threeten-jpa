package com.github.marschall.threeten.jpa.hibernate;

import java.time.OffsetDateTime;

import oracle.sql.TIMESTAMPTZ;

public class OracleOffsetDateTimeType extends AbstractTimestamptzType {

  /**
   * Singleton access
   */
  public static final OracleOffsetDateTimeType INSTANCE = new OracleOffsetDateTimeType();

  @Override
  public Class<?> returnedClass() {
    return OffsetDateTime.class;
  }

  @Override
  Object convertToThreeTen(TIMESTAMPTZ timestamptz) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  TIMESTAMPTZ convertFromThreeTen(Object value) {
    // TODO Auto-generated method stub
    return null;
  }

}
