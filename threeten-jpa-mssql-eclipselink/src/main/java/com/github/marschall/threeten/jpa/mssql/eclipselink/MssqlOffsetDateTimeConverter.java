package com.github.marschall.threeten.jpa.mssql.eclipselink;

import java.time.OffsetDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.marschall.threeten.jpa.mssql.impl.DateTimeOffsetConverter;

import microsoft.sql.DateTimeOffset;

/**
 * Converts {@link DateTimeOffset} to {@link OffsetDateTime} and back.
 */
@Converter(autoApply = true)
public class MssqlOffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, DateTimeOffset> {

  @Override
  public DateTimeOffset convertToDatabaseColumn(OffsetDateTime attribute) {
    return DateTimeOffsetConverter.offsetDateTimeToDateTimeOffset(attribute);
  }

  @Override
  public OffsetDateTime convertToEntityAttribute(DateTimeOffset dbData) {
    return DateTimeOffsetConverter.dateTimeOffsetToOffsetDateTime(dbData);
  }

}
