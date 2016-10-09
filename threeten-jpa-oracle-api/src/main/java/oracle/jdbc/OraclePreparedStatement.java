package oracle.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPTZ;

public interface OraclePreparedStatement extends PreparedStatement {

  void setTIMESTAMPTZ(int paramInt, TIMESTAMPTZ paramTIMESTAMPTZ) throws SQLException;

  void setTIMESTAMPTZAtName(String paramString, TIMESTAMPTZ paramTIMESTAMPTZ) throws SQLException;

  void setTIMESTAMP(int paramInt, TIMESTAMP paramTIMESTAMP) throws SQLException;

  void setTIMESTAMPAtName(String paramString, TIMESTAMP paramTIMESTAMP) throws SQLException;

}
