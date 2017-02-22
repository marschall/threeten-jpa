package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalTime;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * Type for {@link LocalTime}.
 */
public class Jdbc42LocalTimeType extends AbstractThreeTenType {

  /**
   * Singleton access.
   */
  public static final UserType INSTANCE = new Jdbc42LocalTimeType();

  /**
   * Name of the type.
   */
  public static final String NAME = "Jdbc42LocalTimeType";

  private static final int[] SQL_TYPES = new int[]{Types.TIME};

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Class<?> returnedClass() {
    return LocalTime.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
    return rs.getObject(names[0], LocalTime.class);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
    if (value == null) {
      st.setNull(index, Types.TIME);
    } else {
      st.setObject(index, value, Types.TIME);
    }
  }

}
