package com.github.marschall.threeten.jpa.oracle;

import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.marschall.threeten.jpa.oracle.impl.TimestamptzConverter;

import oracle.sql.TIMESTAMPTZ;

/**
 * Converts {@link TIMESTAMPTZ} to {@link ZonedDateTime} and back.
 */
@Converter(autoApply = true)
public class OracleZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, TIMESTAMPTZ> {

  @Override
  public TIMESTAMPTZ convertToDatabaseColumn(ZonedDateTime attribute) {
    return TimestamptzConverter.zonedDateTimeToTimestamptz(attribute);
  }

  @Override
  public ZonedDateTime convertToEntityAttribute(TIMESTAMPTZ dbData) {
    return TimestamptzConverter.timestamptzToZonedDateTime(dbData);
  }

}
