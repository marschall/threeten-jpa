package com.github.marschall.threeten.jpa;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts {@link Timestamp} to {@link LocalDateTime} and back.
 *
 * @deprecated supported out of the box with JPA 2.2
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
  // mapping with java.util.Calendar breaks EclipseLink

  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
    if (attribute == null) {
      return null;
    }
    return Timestamp.valueOf(attribute);
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
    if (dbData == null) {
      return null;
    }
    return dbData.toLocalDateTime();
  }

}
