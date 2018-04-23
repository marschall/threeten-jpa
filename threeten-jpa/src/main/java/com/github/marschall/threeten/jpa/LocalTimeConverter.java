package com.github.marschall.threeten.jpa;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts {@link Time} to {@link LocalTime} and back.
 *
 * @deprecated supported out of the box with JPA 2.2
 */
@Deprecated
@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Time> {
  // mapping with java.util.Calendar breaks EclipseLink

  @Override
  public Time convertToDatabaseColumn(LocalTime attribute) {
    if (attribute == null) {
      return null;
    }
    long epochMilli = attribute.truncatedTo(ChronoUnit.MILLIS)
      .atDate(LocalDate.of(1970, 1, 1))
      .atZone(ZoneId.systemDefault())
      .toInstant()
      .toEpochMilli();
    return new Time(epochMilli);
  }

  @Override
  public LocalTime convertToEntityAttribute(Time dbData) {
    if (dbData == null) {
      return null;
    }

    return dbData.toLocalTime();
  }

}
