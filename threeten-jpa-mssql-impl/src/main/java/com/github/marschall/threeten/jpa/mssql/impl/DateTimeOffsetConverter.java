package com.github.marschall.threeten.jpa.mssql.impl;

import static java.time.ZoneOffset.UTC;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import microsoft.sql.DateTimeOffset;

/**
 * Converts between {@link DateTimeOffset} and {@link OffsetDateTime} times and back.
 */
public final class DateTimeOffsetConverter {

  private DateTimeOffsetConverter() {
    throw new AssertionError("not instantiable");
  }

  public static DateTimeOffset offsetDateTimeToDateTimeOffset(OffsetDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    Timestamp timestamp = Timestamp.from(attribute.toInstant());
    int offsetSeconds = attribute.getOffset().getTotalSeconds();
    return DateTimeOffset.valueOf(timestamp, Math.toIntExact(SECONDS.toMinutes(offsetSeconds)));
  }

  public static OffsetDateTime dateTimeOffsetToOffsetDateTime(DateTimeOffset dbData) {
    if (dbData == null) {
      return null;
    }

    OffsetDateTime utc = OffsetDateTime.ofInstant(dbData.getTimestamp().toInstant(), UTC);
    int offsetSeconds = Math.toIntExact(MINUTES.toSeconds(dbData.getMinutesOffset()));
    ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);

    return utc.withOffsetSameInstant(offset);
  }

}
