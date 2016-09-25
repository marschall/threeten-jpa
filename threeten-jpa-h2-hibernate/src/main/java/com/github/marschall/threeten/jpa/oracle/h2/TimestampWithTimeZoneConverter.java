package com.github.marschall.threeten.jpa.oracle.h2;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.h2.api.TimestampWithTimeZone;
import org.h2.util.DateTimeUtils;

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
    long dateValue = DateTimeUtils.dateValue(attribute.getYear(), attribute.getMonthValue(), attribute.getDayOfMonth());

    OffsetDateTime midnight = attribute.truncatedTo(ChronoUnit.DAYS);
    long nanos = Duration.between(midnight, attribute).toNanos();

    ZoneOffset offset = attribute.getOffset();
    short timeZoneOffsetMins = (short) TimeUnit.SECONDS.toMinutes(offset.getTotalSeconds());

    return new TimestampWithTimeZone(dateValue, nanos, timeZoneOffsetMins);
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
    LocalDate localDate = LocalDate.of(dbData.getYear(), dbData.getMonth(), dbData.getDay());
    LocalDateTime localDateTime = localDate.atStartOfDay().plusNanos(dbData.getNanosSinceMidnight());

    short timeZoneOffsetMins = dbData.getTimeZoneOffsetMins();
    ZoneOffset offset = ZoneOffset.ofTotalSeconds((int) TimeUnit.MINUTES.toSeconds(timeZoneOffsetMins));

    return OffsetDateTime.of(localDateTime, offset);
  }

}
