package com.github.marschall.threeten.jpa.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

import org.eclipse.persistence.internal.helper.ClassConstants;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.platform.database.oracle.Oracle12Platform;

import com.github.marschall.threeten.jpa.oracle.impl.TimestamptzConverter;

import oracle.sql.TIMESTAMPTZ;

/**
 * Work around for <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=511999">Bug 511999</a>.
 */
public class PatchedOracle12Platform extends Oracle12Platform {

  @Override
  public Object getTIMESTAMPTZFromResultSet(ResultSet resultSet, int columnNumber, int type, AbstractSession session)
      throws SQLException {
    return resultSet.getObject(columnNumber);
  }

  @Override
  public Object convertObject(Object sourceObject, Class javaClass) {
    Object valueToConvert;
    if (sourceObject instanceof TIMESTAMPTZ) {
      ZonedDateTime zonedDateTime = TimestamptzConverter.timestamptzToZonedDateTime((TIMESTAMPTZ) sourceObject);
      if (javaClass == ClassConstants.CALENDAR || javaClass == ClassConstants.GREGORIAN_CALENDAR) {
        valueToConvert = GregorianCalendar.from(zonedDateTime);
      } else {
        valueToConvert = Timestamp.valueOf(zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
      }
    } else {
      valueToConvert = sourceObject;
    }
    return super.convertObject(valueToConvert, javaClass);
  }

}
