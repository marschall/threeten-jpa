package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.TIMESTAMPTZ;

/**
 * Abstract base class for types based on {@code TIMESTAMP WITH TIME ZONE}
 * implemented with the Oracle {@code TIMESTAMPTZ} type.
 */
abstract class AbstractTimestamptzType extends AbstractThreeTenType {

  private static final int[] SQL_TYPES = new int []{OracleTypes.TIMESTAMPTZ};

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
    OracleResultSet oracleResultSet = rs.unwrap(OracleResultSet.class);
    TIMESTAMPTZ timestamptz = oracleResultSet.getTIMESTAMPTZ(names[0]);
    if (timestamptz == null) {
      return null;
    } else {
      return convertToThreeTen(timestamptz);
    }
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
    OraclePreparedStatement oraclePreparedStatement = st.unwrap(OraclePreparedStatement.class);
    if (value == null) {
      oraclePreparedStatement.setTIMESTAMPTZ(index, null);
    } else {
      oraclePreparedStatement.setTIMESTAMPTZ(index, convertFromThreeTen(value));
    }
  }



  abstract TIMESTAMPTZ convertFromThreeTen(Object value);

  abstract Object convertToThreeTen(TIMESTAMPTZ timestamptz);

}