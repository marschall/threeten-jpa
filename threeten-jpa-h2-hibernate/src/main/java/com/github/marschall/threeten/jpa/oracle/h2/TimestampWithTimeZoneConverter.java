package com.github.marschall.threeten.jpa.oracle.h2;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import org.h2.api.TimestampWithTimeZone;

/**
 * Converts between {@link TimestampWithTimeZone} and java.time times and back.
 */
final class TimestampWithTimeZoneConverter {

  private TimestampWithTimeZoneConverter() {
    throw new AssertionError("not instantiable");
  }


  /**
   * Converts {@link OffsetDateTime} to {@link TimestampWithTimeZone}.
   *
   * @param attribute the value to be converted, possibly {@code null}
   * @return  the converted data, possibly {@code null}
   */
  static TimestampWithTimeZone offsetDateTimeToTimestampWithTimeZone(OffsetDateTime attribute) {
    if (attribute == null) {
      return null;
    }
    Instant instant = attribute.toInstant();
    ZoneOffset offset = attribute.getOffset();
    long timeMillis = instant.getEpochSecond() * 1_000L;
    int nanos = instant.getNano();
    short timeZoneOffsetMins = (short) TimeUnit.SECONDS.toMinutes(offset.getTotalSeconds());
    return new TimestampWithTimeZone(timeMillis, nanos, timeZoneOffsetMins);
  }

  /**
   * Converts {@link TimestampWithTimeZone} to {@link OffsetDateTime}.
   *
   * @param dbData the data from the database to be converted, possibly {@code null}
   * @return the converted value, possibly {@code null}
   */
  static OffsetDateTime timestampWithTimeZoneToOffsetDateTime(TimestampWithTimeZone dbData) {
    if (dbData == null) {
      return null;
    }
    long epochSecond = dbData.getTime() / 1_000L;
    int nanoAdjustment = dbData.getNanos();
    short timeZoneOffsetMins = dbData.getTimeZoneOffsetMins();
    Instant instant = Instant.ofEpochSecond(epochSecond, nanoAdjustment);
    ZoneOffset offset = ZoneOffset.ofTotalSeconds((int) TimeUnit.MINUTES.toSeconds(timeZoneOffsetMins));
    return OffsetDateTime.ofInstant(instant, offset);
  }

}
