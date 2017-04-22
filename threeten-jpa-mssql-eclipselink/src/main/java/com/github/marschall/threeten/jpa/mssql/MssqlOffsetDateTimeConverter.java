package com.github.marschall.threeten.jpa.mssql;

import static java.time.ZoneOffset.UTC;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import microsoft.sql.DateTimeOffset;

/**
 * Converts {@link DateTimeOffset} to {@link OffsetDateTime} and back.
 */
@Converter(autoApply = true)
public class MssqlOffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, DateTimeOffset> {

  @Override
  public DateTimeOffset convertToDatabaseColumn(OffsetDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    Timestamp timestamp = Timestamp.from(attribute.toInstant());
    int offsetSeconds = attribute.getOffset().getTotalSeconds();
    return DateTimeOffset.valueOf(timestamp, Math.toIntExact(SECONDS.toMinutes(offsetSeconds)));
  }

  @Override
  public OffsetDateTime convertToEntityAttribute(DateTimeOffset dbData) {
    if (dbData == null) {
      return null;
    }

    OffsetDateTime utc = OffsetDateTime.ofInstant(dbData.getTimestamp().toInstant(), UTC);
    int offsetSeconds = Math.toIntExact(MINUTES.toSeconds(dbData.getMinutesOffset()));
    ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);

    return utc.withOffsetSameInstant(offset);
  }

}
