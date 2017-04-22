package com.github.marschall.threeten.jpa.h2;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.h2.api.TimestampWithTimeZone;
import org.h2.util.DateTimeUtils;

final class TimestampWithTimeZoneConverter {

  private TimestampWithTimeZoneConverter() {
    throw new AssertionError("not instantiable");
  }

  static TimestampWithTimeZone offsetDateTimeToTimestampWithTimeZone(OffsetDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    LocalDateTime localDateTime = attribute.toLocalDateTime();
    LocalDate localDate = localDateTime.toLocalDate();
    ZoneOffset zoneOffset = attribute.getOffset();

    int year = localDate.getYear();
    int month = localDate.getMonthValue();
    int day = localDate.getDayOfMonth();
    long dateValue = DateTimeUtils.dateValue(year, month, day);

    LocalDateTime midnight = localDateTime.truncatedTo(DAYS);
    Duration duration = Duration.between(midnight, localDateTime);
    long timeNanos = duration.toNanos();

    int totalSeconds = zoneOffset.getTotalSeconds();
    short timeZoneOffsetMins = toShortExact(SECONDS.toMinutes(totalSeconds));

    return new TimestampWithTimeZone(dateValue, timeNanos, timeZoneOffsetMins);
  }

  static OffsetDateTime timestampWithTimeZoneToOffsetDateTime(TimestampWithTimeZone dbData) {
    if (dbData == null) {
      return null;
    }

    long dateValue = dbData.getYMD();

    int year = DateTimeUtils.yearFromDateValue(dateValue);
    int month = DateTimeUtils.monthFromDateValue(dateValue);
    int day = DateTimeUtils.dayFromDateValue(dateValue);
    LocalDate localDate = LocalDate.of(year, month, day);

    long timeNanos = dbData.getNanosSinceMidnight();
    LocalDateTime localDateTime = localDate.atStartOfDay().plusNanos(timeNanos);

    short timeZoneOffsetMins = dbData.getTimeZoneOffsetMins();
    int offsetSeconds = Math.toIntExact(MINUTES.toSeconds(timeZoneOffsetMins));
    ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);

    return OffsetDateTime.of(localDateTime, offset);
  }

  private static short toShortExact(long l) {
    if (l > Short.MAX_VALUE) {
      throw new AssertionError();
    }
    if (l < Short.MIN_VALUE) {
      throw new AssertionError();
    }
    return (short) l;
  }

}
