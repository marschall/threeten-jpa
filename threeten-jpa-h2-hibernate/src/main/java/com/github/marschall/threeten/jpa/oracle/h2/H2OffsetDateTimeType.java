package com.github.marschall.threeten.jpa.oracle.h2;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import org.h2.api.TimestampWithTimeZone;


/**
 * Type for {@link OffsetDateTime}.
 */
public class H2OffsetDateTimeType {

  private H2OffsetDateTimeType() {
    throw new AssertionError("not instantiable");
  }


  /**
   * Converts {@link OffsetDateTime} to {@link TimestampWithTimeZone}.
   *
   * @param attribute the value to be converted, possibly {@code null}
   * @return  the converted data, possibly {@code null}
   */
  public static TimestampWithTimeZone offsetDateTimeToTimestamptz(OffsetDateTime attribute) {
    if (attribute == null) {
      return null;
    }
    Instant instant = attribute.toInstant();
    ZoneOffset offset = attribute.getOffset();
    long timeMillis = instant.getEpochSecond();
    int nanos = instant.getNano(); // FIXME
    short timeZoneOffsetMins = (short) TimeUnit.SECONDS.toMinutes(offset.getTotalSeconds());
    return new TimestampWithTimeZone(timeMillis, nanos, timeZoneOffsetMins);
  }

  /**
   * Converts {@link TimestampWithTimeZone} to {@link OffsetDateTime}.
   *
   * @param dbData the data from the database to be converted, possibly {@code null}
   * @return the converted value, possibly {@code null}
   */
  public static OffsetDateTime timestamptzToOffsetDateTime(TimestampWithTimeZone dbData) {
    if (dbData == null) {
      return null;
    }
    long timeMillis = dbData.getTime();
    int nanos = dbData.getNanos();
    short timeZoneOffsetMins = dbData.getTimeZoneOffsetMins();
    Instant instant = Instant.ofEpochSecond(timeMillis, nanos); // FIXME
    ZoneOffset offset = ZoneOffset.ofTotalSeconds((int) TimeUnit.MINUTES.toSeconds(timeZoneOffsetMins));
    return OffsetDateTime.ofInstant(instant, offset);
  }

}
