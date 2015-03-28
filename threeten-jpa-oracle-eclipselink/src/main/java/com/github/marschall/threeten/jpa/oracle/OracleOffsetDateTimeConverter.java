package com.github.marschall.threeten.jpa.oracle;

import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.extractOffset;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.extractUtc;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.extractZoneId;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.isFixedOffset;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.newBuffer;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.writeDateTime;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.writeZoneOffset;
import static java.time.ZoneOffset.UTC;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import oracle.sql.TIMESTAMPTZ;

/**
 * Converts {@link TIMESTAMPTZ} to {@link OffsetDateTime} and back.
 */
@Converter(autoApply = true)
public class OracleOffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, TIMESTAMPTZ> {


  @Override
  public TIMESTAMPTZ convertToDatabaseColumn(OffsetDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    byte[] bytes = newBuffer();
    ZonedDateTime utc = attribute.atZoneSameInstant(UTC);
    writeDateTime(bytes, utc);

    ZoneOffset offset = attribute.getOffset();
    writeZoneOffset(bytes, offset);

    return new TIMESTAMPTZ(bytes);
  }

  @Override
  public OffsetDateTime convertToEntityAttribute(TIMESTAMPTZ dbData) {
    if (dbData == null) {
      return null;
    }

    byte[] bytes = dbData.toBytes();
    OffsetDateTime utc = extractUtc(bytes);
    if (isFixedOffset(bytes)) {
      ZoneOffset offset = extractOffset(bytes);
      return utc.withOffsetSameInstant(offset);
    } else {
      ZoneId zoneId = extractZoneId(bytes);
      return utc.atZoneSameInstant(zoneId).toOffsetDateTime();
    }
  }

}
