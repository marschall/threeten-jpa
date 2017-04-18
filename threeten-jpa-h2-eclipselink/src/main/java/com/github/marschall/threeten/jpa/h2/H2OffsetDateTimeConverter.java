package com.github.marschall.threeten.jpa.h2;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.h2.api.TimestampWithTimeZone;
import org.h2.util.DateTimeUtils;

/**
 * Converts {@link TimestampWithTimeZone} to {@link OffsetDateTime} and back.
 */
@Converter(autoApply = true)
public class H2OffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, TimestampWithTimeZone> {

  @Override
  public TimestampWithTimeZone convertToDatabaseColumn(OffsetDateTime attribute) {
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


    LocalDateTime midnight = localDateTime.truncatedTo(ChronoUnit.DAYS);
    Duration duration = Duration.between(midnight, localDateTime);
    long timeNanos = duration.toNanos();

    int totalSeconds = zoneOffset.getTotalSeconds();
    short timeZoneOffsetMins = toShortExact(TimeUnit.SECONDS.toMinutes(totalSeconds));

    return new TimestampWithTimeZone(dateValue, timeNanos, timeZoneOffsetMins);
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

  @Override
  public OffsetDateTime convertToEntityAttribute(TimestampWithTimeZone dbData) {
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
    int offsetSeconds = Math.toIntExact(TimeUnit.MINUTES.toSeconds(timeZoneOffsetMins));
    ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);
    return OffsetDateTime.of(localDateTime, offset);
  }

}
