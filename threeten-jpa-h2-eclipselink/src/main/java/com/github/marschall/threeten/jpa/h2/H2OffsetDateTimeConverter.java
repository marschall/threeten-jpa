package com.github.marschall.threeten.jpa.h2;

import java.time.OffsetDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.h2.api.TimestampWithTimeZone;

/**
 * Converts {@link TimestampWithTimeZone} to {@link OffsetDateTime} and back.
 */
@Converter(autoApply = true)
public class H2OffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, TimestampWithTimeZone> {

  @Override
  public TimestampWithTimeZone convertToDatabaseColumn(OffsetDateTime attribute) {
    return TimestampWithTimeZoneConverter.offsetDateTimeToTimestampWithTimeZone(attribute);
  }

  @Override
  public OffsetDateTime convertToEntityAttribute(TimestampWithTimeZone dbData) {
    return TimestampWithTimeZoneConverter.timestampWithTimeZoneToOffsetDateTime(dbData);
  }

}
