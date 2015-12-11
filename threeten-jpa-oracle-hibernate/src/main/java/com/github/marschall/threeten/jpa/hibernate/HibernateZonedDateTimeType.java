package com.github.marschall.threeten.jpa.hibernate;

import java.time.ZonedDateTime;

import oracle.sql.TIMESTAMPTZ;

public class HibernateZonedDateTimeType extends AbstractHibernateTimestamptzType {

  /**
   * Singleton access
   */
  public static final HibernateZonedDateTimeType INSTANCE = new HibernateZonedDateTimeType();

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
