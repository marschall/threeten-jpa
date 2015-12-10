package oracle.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.TIMESTAMPTZ;

public interface OracleResultSet extends ResultSet {

  TIMESTAMPTZ getTIMESTAMPTZ(int paramInt) throws SQLException;

  TIMESTAMPTZ getTIMESTAMPTZ(String paramString) throws SQLException;

}
