package com.github.marschall.threeten.jpa.h2;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.platform.database.H2Platform;
import org.h2.api.TimestampWithTimeZone;

/**
 * Work around for <a href="https://github.com/h2database/h2database/pull/496">H2#496</a>.
 *
 * @deprecated no longer needed as of H2 1.4.195
 */
@Deprecated
public class PatchedH2Platform extends H2Platform {

  @Override
  public void setParameterValueInDatabaseCall(Object parameter,
          PreparedStatement statement, int index, AbstractSession session)
          throws SQLException {
    if (parameter instanceof TimestampWithTimeZone) {
      OffsetDateTime timestampWithTimeZone = TimestampWithTimeZoneConverter.timestampWithTimeZoneToOffsetDateTime((TimestampWithTimeZone) parameter);
      statement.setObject(index, timestampWithTimeZone);
    } else {
      super.setParameterValueInDatabaseCall(parameter, statement, index, session);
    }
  }

}
