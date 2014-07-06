package com.github.marschall.threeten.jpa;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts {@link Time} to {@link LocalTime} and back.
 */
@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Time> {
  // mapping with java.util.Calendar breaks EclipseLink

  @Override
  public Time convertToDatabaseColumn(LocalTime attribute) {
    if (attribute == null) {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(Calendar.YEAR, 1970);
    // avoid 0 vs 1 based months
    calendar.set(Calendar.DAY_OF_YEAR, 1);
    calendar.set(Calendar.HOUR_OF_DAY, attribute.getHour());
    calendar.set(Calendar.MINUTE, attribute.getMinute());
    calendar.set(Calendar.MILLISECOND, (int) (attribute.getNano() / 1000000L));
    return new Time(calendar.getTimeInMillis());
  }
  
  @Override
  public LocalTime convertToEntityAttribute(Time dbData) {
    if (dbData == null) {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dbData);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    int nanoOfSecond = calendar.get(Calendar.MILLISECOND) * 1000000;
    return LocalTime.of(hour, minute, second, nanoOfSecond);
  }

}
