package com.github.marschall.threeten.jpa;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts {@link Timestamp} to {@link Instant} and back.
 */
@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(Instant attribute) {
    if (attribute == null) {
      return null;
    }
    return Timestamp.from(attribute);
  }

  @Override
  public Instant convertToEntityAttribute(Timestamp dbData) {
    if (dbData == null) {
      return null;
    }
    return dbData.toInstant();
  }


}
