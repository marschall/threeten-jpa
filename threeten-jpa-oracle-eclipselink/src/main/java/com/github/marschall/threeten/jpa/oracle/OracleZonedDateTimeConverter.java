package com.github.marschall.threeten.jpa.oracle;

import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.extractOffset;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.extractUtc;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.extractZoneId;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.isFixedOffset;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.isValidRegionCode;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.newBuffer;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.writeDateTime;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.writeZoneId;
import static com.github.marschall.threeten.jpa.oracle.OracleTimeConverter.writeZoneOffset;
import static java.time.ZoneOffset.UTC;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import oracle.sql.TIMESTAMPTZ;
import oracle.sql.ZONEIDMAP;

/**
 * Converts {@link TIMESTAMPTZ} to {@link ZonedDateTime} and back.
 */
@Converter(autoApply = true)
public class OracleZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, TIMESTAMPTZ> {

  @Override
  public TIMESTAMPTZ convertToDatabaseColumn(ZonedDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    byte[] bytes = newBuffer();
    ZonedDateTime utc = attribute.withZoneSameInstant(UTC);
    writeDateTime(bytes, utc);

    String zoneId = attribute.getZone().getId();
    int regionCode = ZONEIDMAP.getID(zoneId);
    
    if (isValidRegionCode(regionCode)) {
      writeZoneId(bytes, regionCode);
    } else {
      writeZoneOffset(bytes, attribute.getOffset());
    }
    

    return new TIMESTAMPTZ(bytes);
  }

  @Override
  public ZonedDateTime convertToEntityAttribute(TIMESTAMPTZ dbData) {
    if (dbData == null) {
      return null;
    }
    
    byte[] bytes = dbData.toBytes();
    OffsetDateTime utc = extractUtc(bytes);
    if (isFixedOffset(bytes)) {
      ZoneOffset offset = extractOffset(bytes);
      return utc.atZoneSameInstant(offset);
    } else {
      ZoneId zoneId = extractZoneId(bytes);
      return utc.atZoneSameInstant(zoneId);
    }
  }

}
