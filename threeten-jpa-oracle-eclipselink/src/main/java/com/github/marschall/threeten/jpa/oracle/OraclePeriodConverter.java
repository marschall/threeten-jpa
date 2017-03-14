package com.github.marschall.threeten.jpa.oracle;

import java.time.Period;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.marschall.threeten.jpa.oracle.impl.IntervalConverter;

import oracle.sql.INTERVALYM;

/**
 * Converts {@link INTERVALYM} to {@link Period} and back.
 */
@Converter(autoApply = true)
public class OraclePeriodConverter implements AttributeConverter<Period, INTERVALYM> {

  @Override
  public INTERVALYM convertToDatabaseColumn(Period attribute) {
    return IntervalConverter.periodToIntervalym(attribute);
  }

  @Override
  public Period convertToEntityAttribute(INTERVALYM dbData) {
    return IntervalConverter.intervalymToPeriod(dbData);
  }

}
