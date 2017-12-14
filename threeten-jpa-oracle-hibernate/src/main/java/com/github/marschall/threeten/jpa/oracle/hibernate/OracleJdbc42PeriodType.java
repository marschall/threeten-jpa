package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Period;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import oracle.jdbc.OracleTypes;

/**
 * Type mapping {@link Period} to {@code INTERVALYM}.
 */
public class OracleJdbc42PeriodType extends AbstractThreeTenType {

  /**
   * Singleton access.
   */
  public static final UserType INSTANCE = new OracleJdbc42PeriodType();

  /**
   * Name of the type.
   */
  public static final String NAME = "OracleJdbc42PeriodType";

  private static final int[] SQL_TYPES = new int []{OracleTypes.INTERVALYM};

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Class<?> returnedClass() {
    return Period.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
    return rs.getObject(names[0], Period.class);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
    if (value == null) {
      st.setNull(index, OracleTypes.INTERVALYM);
    } else {
      st.setObject(index, value, OracleTypes.INTERVALYM);
    }
  }

}
