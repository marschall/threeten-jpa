package oracle.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPTZ;

public interface OracleResultSet extends ResultSet {

  TIMESTAMP getTIMESTAMP(int paramInt) throws SQLException;

  TIMESTAMP getTIMESTAMP(String paramString) throws SQLException;

  TIMESTAMPTZ getTIMESTAMPTZ(int paramInt) throws SQLException;

  TIMESTAMPTZ getTIMESTAMPTZ(String paramString) throws SQLException;

  INTERVALDS getINTERVALDS(int paramInt) throws SQLException;

  INTERVALDS getINTERVALDS(String paramString) throws SQLException;

  INTERVALYM getINTERVALYM(int paramInt) throws SQLException;

  INTERVALYM getINTERVALYM(String paramString) throws SQLException;

}
