package com.github.marschall.threeten.jpa;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {
  // mapping with java.util.Calendar breaks EclipseLink

  @Override
  public Date convertToDatabaseColumn(LocalDate attribute) {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(Calendar.YEAR, attribute.getYear());
    // avoid 0 vs 1 based months
    calendar.set(Calendar.DAY_OF_YEAR, attribute.getDayOfYear());
    return new Date(calendar.getTimeInMillis());
  }

  @Override
  public LocalDate convertToEntityAttribute(Date dbData) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dbData);
    int year = calendar.get(Calendar.YEAR);
    // avoid 0 vs 1 based months
    int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
    return LocalDate.ofYearDay(year, dayOfYear);
  }

}
