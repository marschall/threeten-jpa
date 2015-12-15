package com.github.marschall.threeten.jpa.oracle;

import java.time.OffsetDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.marschall.threeten.jpa.oracle.impl.TimestamptzConverter;

import oracle.sql.TIMESTAMPTZ;

/**
 * Converts {@link TIMESTAMPTZ} to {@link OffsetDateTime} and back.
 */
@Converter(autoApply = true)
public class OracleOffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, TIMESTAMPTZ> {


  @Override
  public TIMESTAMPTZ convertToDatabaseColumn(OffsetDateTime attribute) {
    return TimestamptzConverter.offsetDateTimeToTimestamptz(attribute);
  }

  @Override
  public OffsetDateTime convertToEntityAttribute(TIMESTAMPTZ dbData) {
    return TimestamptzConverter.timestamptzToOffsetDateTime(dbData);
  }

}
