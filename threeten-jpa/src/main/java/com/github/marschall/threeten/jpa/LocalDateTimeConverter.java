package com.github.marschall.threeten.jpa;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts {@link Timestamp} to {@link LocalDateTime} and back.
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
  // mapping with java.util.Calendar breaks EclipseLink

  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(Calendar.YEAR, attribute.getYear());
    // avoid 0 vs 1 based months
    calendar.set(Calendar.DAY_OF_YEAR, attribute.getDayOfYear());
    calendar.set(Calendar.HOUR_OF_DAY, attribute.getHour());
    calendar.set(Calendar.MINUTE, attribute.getMinute());
    calendar.set(Calendar.SECOND, attribute.getSecond());
    calendar.set(Calendar.MILLISECOND, (int) (attribute.getNano() / 1000000L));
    return new Timestamp(calendar.getTimeInMillis());
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
    if (dbData == null) {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dbData);
    // convert 0 vs 1 based months
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    int nanoOfSecond = calendar.get(Calendar.MILLISECOND) * 1000000;
    return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond);
  }

}
