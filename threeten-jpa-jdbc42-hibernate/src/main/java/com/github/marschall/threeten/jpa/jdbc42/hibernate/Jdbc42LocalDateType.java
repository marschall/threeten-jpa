package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import org.hibernate.engine.spi.SessionImplementor;

public class Jdbc42LocalDateType extends AbstractThreeTenType {

  /**
   * Singleton access
   */
  public static final Jdbc42LocalDateType INSTANCE = new Jdbc42LocalDateType();

  /**
   * Name of the type.
   */
  public static final String NAME = "Jdbc42LocalDateType";

  private static final int[] SQL_TYPES = new int[]{Types.DATE};

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Class<?> returnedClass() {
    return LocalDate.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
    return rs.getObject(names[0], LocalDate.class);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws SQLException {
    if (value == null) {
      st.setNull(index, Types.DATE);
    } else {
      st.setObject(index, value, Types.DATE);
    }
  }

}
