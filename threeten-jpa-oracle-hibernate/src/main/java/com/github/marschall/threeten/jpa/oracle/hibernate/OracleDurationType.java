package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.github.marschall.threeten.jpa.oracle.impl.IntervalConverter;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.INTERVALDS;

/**
 * Type mapping {@link Duration} to {@code INTERVALDS}.
 *
 * @deprecated use ojdbc8 12.2.0.1 and {@link OracleJdbc42DurationType}
 */
@Deprecated
public class OracleDurationType extends AbstractThreeTenType {

  /**
   * Singleton access.
   */
  public static final UserType INSTANCE = new OracleDurationType();

  /**
   * Name of the type.
   */
  public static final String NAME = "OracleDurationType";

  private static final int[] SQL_TYPES = new int []{OracleTypes.INTERVALDS};

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Class<?> returnedClass() {
    return Duration.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
    OracleResultSet oracleResultSet = rs.unwrap(OracleResultSet.class);
    INTERVALDS intervalds = oracleResultSet.getINTERVALDS(names[0]);
    if (intervalds == null) {
      return null;
    } else {
      return IntervalConverter.intervaldsToDuration(intervalds);
    }
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
    OraclePreparedStatement oraclePreparedStatement = st.unwrap(OraclePreparedStatement.class);
    if (value == null) {
      oraclePreparedStatement.setINTERVALDS(index, null);
    } else {
      INTERVALDS intervalds = IntervalConverter.durationToIntervalds((Duration) value);
      oraclePreparedStatement.setINTERVALDS(index, intervalds);
    }
  }

}
