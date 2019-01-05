package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Period;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.github.marschall.threeten.jpa.oracle.impl.IntervalConverter;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.INTERVALYM;

/**
 * Type mapping {@link Period} to {@code INTERVALYM}.
 *
 * @deprecated use ojdbc8 12.2.0.1 and {@link OracleJdbc42PeriodType}.
 */
@Deprecated
public class OraclePeriodType extends AbstractThreeTenType {

  /**
   * Singleton access.
   */
  public static final UserType INSTANCE = new OraclePeriodType();

  /**
   * Name of the type.
   */
  public static final String NAME = "OraclePeriodType";

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
    OracleResultSet oracleResultSet = rs.unwrap(OracleResultSet.class);
    INTERVALYM intervalym = oracleResultSet.getINTERVALYM(names[0]);
    if (intervalym == null) {
      return null;
    } else {
      return IntervalConverter.intervalymToPeriod(intervalym);
    }
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
    OraclePreparedStatement oraclePreparedStatement = st.unwrap(OraclePreparedStatement.class);
    if (value == null) {
      oraclePreparedStatement.setINTERVALYM(index, null);
    } else {
      INTERVALYM intervalym = IntervalConverter.periodToIntervalym((Period) value);
      oraclePreparedStatement.setINTERVALYM(index, intervalym);
    }
  }

}
