package com.github.marschall.threeten.jpa;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, attribute.getYear());
    // avoid 0 vs 1 based months
    calendar.set(Calendar.DAY_OF_YEAR, attribute.getDayOfYear());
    calendar.set(Calendar.HOUR_OF_DAY, attribute.getHour());
    calendar.set(Calendar.MINUTE, attribute.getMinute());
    calendar.set(Calendar.MILLISECOND, (int) (attribute.getNano() / 1000000L));
    return new Timestamp(calendar.getTimeInMillis());
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dbData);
    // convert 0 vs 1 based months
    return LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND) * 1000000);
  }
//  @Override
//  public Calendar convertToDatabaseColumn(LocalDateTime attribute) {
//    Calendar calendar = Calendar.getInstance();
//    calendar.set(Calendar.YEAR, attribute.getYear());
//    // avoid 0 vs 1 based months
//    calendar.set(Calendar.DAY_OF_YEAR, attribute.getDayOfYear());
//    calendar.set(Calendar.HOUR_OF_DAY, attribute.getHour());
//    calendar.set(Calendar.MINUTE, attribute.getMinute());
//    calendar.set(Calendar.MILLISECOND, (int) (attribute.getNano() / 1000000L));
//    return calendar;
//  }
//  
//  @Override
//  public LocalDateTime convertToEntityAttribute(Calendar dbData) {
//    // convert 0 vs 1 based months
//    return LocalDateTime.of(dbData.get(Calendar.YEAR), dbData.get(Calendar.MONTH) + 1, dbData.get(Calendar.DAY_OF_MONTH), dbData.get(Calendar.HOUR_OF_DAY), dbData.get(Calendar.MINUTE), dbData.get(Calendar.SECOND), dbData.get(Calendar.MILLISECOND) * 1000000);
//  }

}
