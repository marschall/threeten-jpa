package oracle.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPTZ;

public interface OraclePreparedStatement extends PreparedStatement {

  void setTIMESTAMPTZ(int paramInt, TIMESTAMPTZ paramTIMESTAMPTZ) throws SQLException;

  void setTIMESTAMPTZAtName(String paramString, TIMESTAMPTZ paramTIMESTAMPTZ) throws SQLException;

  void setTIMESTAMP(int paramInt, TIMESTAMP paramTIMESTAMP) throws SQLException;

  void setTIMESTAMPAtName(String paramString, TIMESTAMP paramTIMESTAMP) throws SQLException;

  void setINTERVALDS(int parameterIndex, INTERVALDS x) throws SQLException;

  void  setINTERVALDSAtName(String parameterName, INTERVALDS value) throws SQLException;

  void  setINTERVALYM(int parameterIndex, INTERVALYM x) throws SQLException;

  void  setINTERVALYMAtName(String parameterName, INTERVALYM value) throws SQLException;

}
