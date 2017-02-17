package com.github.marschall.threeten.jpa.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.platform.database.oracle.Oracle11Platform;

/**
 * Work around for <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=511999">Bug 511999</a>.
 */
public class PatchedOracle11Platform extends Oracle11Platform {

  public PatchedOracle11Platform() {
    super();
  }

  @Override
  public Object getTIMESTAMPTZFromResultSet(ResultSet resultSet, int columnNumber, int type, AbstractSession session)
      throws SQLException {
    return resultSet.getObject(columnNumber);
  }

}
