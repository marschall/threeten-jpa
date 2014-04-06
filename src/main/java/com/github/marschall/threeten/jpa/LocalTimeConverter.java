package com.github.marschall.threeten.jpa;

import java.time.LocalTime;
import java.util.Calendar;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Calendar> {

  @Override
  public Calendar convertToDatabaseColumn(LocalTime attribute) {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    // avoid 0 vs 1 based months
    calendar.set(Calendar.YEAR, 1970);
    calendar.set(Calendar.DAY_OF_YEAR, 1);
    calendar.set(Calendar.HOUR_OF_DAY, attribute.getHour());
    calendar.set(Calendar.MINUTE, attribute.getMinute());
    calendar.set(Calendar.MILLISECOND, (int) (attribute.getNano() / 1000000L));
    return calendar;
  }

  @Override
  public LocalTime convertToEntityAttribute(Calendar dbData) {
    // avoid 0 vs 1 based months
    return LocalTime.of(dbData.get(Calendar.HOUR_OF_DAY), dbData.get(Calendar.MINUTE), dbData.get(Calendar.SECOND), dbData.get(Calendar.MILLISECOND) * 1000000);
  }

}
