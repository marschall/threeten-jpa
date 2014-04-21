package com.github.marschall.threeten.jpa.oracle;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.PersistenceException;

import oracle.sql.TIMESTAMPTZ;

@Converter(autoApply = true)
public class OracleOffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, TIMESTAMPTZ> {
  
  // magic
  private static int SIZE_TIMESTAMPTZ = 13;
  private static int OFFSET_HOUR = 20;
  private static int OFFSET_MINUTE = 60;
  private static byte REGIONIDBIT = -128;
  
  // Byte 0: Century, offset is 100 (value - 100 is century)
  // Byte 1: Decade, offset is 100 (value - 100 is decade)
  // Byte 2: Month UTC
  // Byte 3: Day UTC
  // Byte 4: Hour UTC, offset is 1 (value-1 is UTC hour)
  // Byte 5: Minute UTC, offset is 1 (value-1 is UTC Minute)
  // Byte 6: Second, offset is 1 (value-1 is seconds)
  // Byte 7: nanoseconds (most significant bit)
  // Byte 8: nanoseconds
  // Byte 9: nanoseconds
  // Byte 10: nanoseconds (least significant bit)
  // Byte 11: Hour UTC-offset of Timezone, offset is 20 (value-20 is UTC-hour offset)
  // Byte 12: Minute UTC-offset of Timezone, offset is 60 (value-60 is UTC-minute offset)


  @Override
  public TIMESTAMPTZ convertToDatabaseColumn(OffsetDateTime attribute) {
    byte[] bytes = new byte[SIZE_TIMESTAMPTZ];
    int year = attribute.getYear();
    bytes[0] = (byte) (year / 100 + 100);
    bytes[1] = (byte) (year % 100 + 100);
    
    bytes[2] = (byte) attribute.getMonthValue();
    bytes[3] = (byte) attribute.getDayOfMonth();
    bytes[4] = (byte) (attribute.getHour() + 1);
    bytes[5] = (byte) (attribute.getMinute() + 1);
    bytes[6] = (byte) (attribute.getSecond() + 1);
    
    int nano = attribute.getNano();
    bytes[7] = (byte) (nano >> 24);
    bytes[8] = (byte) (nano >> 16 & 0xFF);
    bytes[9] = (byte) (nano >> 8 & 0xFF);
    bytes[10] = (byte) (nano & 0xFF);
    
    ZoneOffset offset = attribute.getOffset();
    bytes[11] = (byte) (offset.get(ChronoField.HOUR_OF_DAY) + OFFSET_HOUR);
    bytes[12] = (byte) (offset.get(ChronoField.MINUTE_OF_HOUR) + OFFSET_MINUTE);
    
    return new TIMESTAMPTZ(bytes);
  }

  @Override
  public OffsetDateTime convertToEntityAttribute(TIMESTAMPTZ dbData) {
    byte[] bytes = dbData.toBytes();
    int year = ((bytes[0] - 100) * 100) + (bytes[1] - 100);
    int month = bytes[2];
    int dayOfMonth = bytes[3];
    int hour = bytes[4] -1;
    int minute = bytes[5] - 1;
    int second = bytes[6] - 1;
    int nanoOfSecond = bytes[7] << 24 | bytes[8] << 16 | bytes[9] << 8 | bytes[10];
    if ((bytes[11] & REGIONIDBIT) == 0) {
      ZoneOffset offset = ZoneOffset.ofHoursMinutes(bytes[11], bytes[12]);
      return OffsetDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, offset);
    } else {
      throw new PersistenceException("can not convert time zone id to offset");
    }
  }

}
