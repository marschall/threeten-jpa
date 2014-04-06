package com.github.marschall.threeten.jpa;

import java.time.LocalDate;
import java.util.Calendar;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Calendar> {

  @Override
  public Calendar convertToDatabaseColumn(LocalDate attribute) {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    // avoid 0 vs 1 based months
    calendar.set(Calendar.YEAR, attribute.getYear());
    calendar.set(Calendar.DAY_OF_YEAR, attribute.getDayOfYear());
    return null;
  }

  @Override
  public LocalDate convertToEntityAttribute(Calendar dbData) {
    // avoid 0 vs 1 based months
    return LocalDate.ofYearDay(dbData.get(Calendar.YEAR), dbData.get(Calendar.DAY_OF_YEAR));
  }

}
