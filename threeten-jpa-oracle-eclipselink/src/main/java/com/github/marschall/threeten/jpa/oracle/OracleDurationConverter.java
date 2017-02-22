package com.github.marschall.threeten.jpa.oracle;

import java.time.Duration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.marschall.threeten.jpa.oracle.impl.IntervalConverter;

import oracle.sql.INTERVALDS;

/**
 * Converts {@link INTERVALDS} to {@link Duration} and back.
 */
@Converter(autoApply = true)
public class OracleDurationConverter implements AttributeConverter<Duration, INTERVALDS> {

  @Override
  public INTERVALDS convertToDatabaseColumn(Duration attribute) {
    return IntervalConverter.durationToIntervalds(attribute);
  }

  @Override
  public Duration convertToEntityAttribute(INTERVALDS dbData) {
    return IntervalConverter.intervaldsToDuration(dbData);
  }

}
