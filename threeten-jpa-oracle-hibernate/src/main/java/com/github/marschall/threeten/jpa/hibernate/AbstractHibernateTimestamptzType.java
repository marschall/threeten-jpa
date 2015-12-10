package com.github.marschall.threeten.jpa.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.TIMESTAMPTZ;

public abstract class AbstractHibernateTimestamptzType extends AbstraceThreeTenType {

  @Override
  public int[] sqlTypes() {
    return new int []{Types.TIMESTAMP_WITH_TIMEZONE, OracleTypes.TIMESTAMPTZ};
  }



  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    OracleResultSet oracleResultSet = rs.unwrap(OracleResultSet.class);
    TIMESTAMPTZ timestamptz = oracleResultSet.getTIMESTAMPTZ(names[0]);
    if (timestamptz == null) {
      return null;
    } else {
      return convertToThreeTen(timestamptz);
    }
  }



  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
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