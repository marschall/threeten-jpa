package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

/**
 * Abstract base class for all JSR-310 types based on {@code TIMESTAMP WITH TIME ZONE}
 */
abstract class AbstractThreeTenTimestampWithTimeZoneType extends AbstractThreeTenType {

  private static final int[] SQL_TYPES = new int[]{Types.TIMESTAMP_WITH_TIMEZONE};

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
    if (value == null) {
      st.setNull(index, Types.TIMESTAMP_WITH_TIMEZONE);
    } else {
      st.setObject(index, value, Types.TIMESTAMP_WITH_TIMEZONE);
    }
  }

}
