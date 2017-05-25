package com.github.marschall.threeten.jpa.oracle.impl;

import static java.lang.Byte.toUnsignedInt;
import static java.time.ZoneOffset.UTC;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.ZONEIDMAP;

/**
 * Converts between {@link TIMESTAMPTZ}/{@link TIMESTAMP} and java.time times and back.
 */
public final class TimestamptzConverter {

  private static final int INV_ZONEID = -1;

  // magic
  private static final int SIZE_TIMESTAMPTZ = 13;
  private static final int SIZE_TIMESTAMP = 11;
  private static final int OFFSET_HOUR = 20;
  private static final int OFFSET_MINUTE = 60;
  private static final byte REGIONIDBIT = (byte) 0b1000_0000; // -128

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

  /**
   * Converts {@link OffsetDateTime} to {@link TIMESTAMPTZ}.
   *
   * @param attribute the value to be converted, possibly {@code null}
   * @return the converted data, possibly {@code null}
   */
  public static TIMESTAMPTZ offsetDateTimeToTimestamptz(OffsetDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    byte[] bytes = newTimestamptzBuffer();
    ZonedDateTime utc = attribute.atZoneSameInstant(UTC);
    writeDateTime(bytes, utc.toLocalDateTime());

    ZoneOffset offset = attribute.getOffset();
    writeZoneOffset(bytes, offset);

    return new TIMESTAMPTZ(bytes);
  }

  /**
   * Converts {@link TIMESTAMPTZ} to {@link OffsetDateTime}.
   *
   * @param dbData the data from the database to be converted, possibly {@code null}
   * @return the converted value, possibly {@code null}
   */
  public static OffsetDateTime timestamptzToOffsetDateTime(TIMESTAMPTZ dbData) {
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

  /**
   * Converts {@link ZonedDateTime} to {@link TIMESTAMPTZ}.
   *
   * @param attribute the value to be converted, possibly {@code null}
   * @return  the converted data, possibly {@code null}
   */
  public static TIMESTAMPTZ zonedDateTimeToTimestamptz(ZonedDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    byte[] bytes = newTimestamptzBuffer();
    ZonedDateTime utc = attribute.withZoneSameInstant(UTC);
    writeDateTime(bytes, utc.toLocalDateTime());

    String zoneId = attribute.getZone().getId();
    int regionCode = ZONEIDMAP.getID(zoneId);

    if (isValidRegionCode(regionCode)) {
      writeZoneId(bytes, regionCode);
    } else {
      writeZoneOffset(bytes, attribute.getOffset());
    }


    return new TIMESTAMPTZ(bytes);
  }

  /**
   * Converts {@link TIMESTAMPTZ} to {@link ZonedDateTime}.
   *
   * @param dbData the data from the database to be converted, possibly {@code null}
   * @return the converted value, possibly {@code null}
   */
  public static ZonedDateTime timestamptzToZonedDateTime(TIMESTAMPTZ dbData) {
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

  /**
   * Converts {@link LocalDateTime} to {@link TIMESTAMP}.
   *
   * @param attribute the value to be converted, possibly {@code null}
   * @return  the converted data, possibly {@code null}
   */
  public static TIMESTAMP localDateTimeToTimestamp(LocalDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    byte[] bytes = newTimestampBuffer();
    writeDateTime(bytes, attribute);

    return new TIMESTAMP(bytes);
  }

  /**
   * Converts {@link TIMESTAMP} to {@link LocalDateTime}.
   *
   * @param dbData the data from the database to be converted, possibly {@code null}
   * @return the converted value, possibly {@code null}
   */
  public static LocalDateTime timestampToLocalDateTime(TIMESTAMP dbData) {
    if (dbData == null) {
      return null;
    }

    byte[] bytes = dbData.toBytes();
    return extractLocalDateTime(bytes);
  }

  private static LocalDateTime extractLocalDateTime(byte[] bytes) {
    int year = ((toUnsignedInt(bytes[0]) - 100) * 100) + (toUnsignedInt(bytes[1]) - 100);
    int month = bytes[2];
    int dayOfMonth = bytes[3];
    int hour = bytes[4] - 1;
    int minute = bytes[5] - 1;
    int second = bytes[6] - 1;
    int nanoOfSecond = toUnsignedInt(bytes[7]) << 24
            | toUnsignedInt(bytes[8]) << 16
            | toUnsignedInt(bytes[9]) << 8
            | toUnsignedInt(bytes[10]);
    return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond);
  }

  private static OffsetDateTime extractUtc(byte[] bytes) {
    return OffsetDateTime.of(extractLocalDateTime(bytes), UTC);
  }

  private static boolean isFixedOffset(byte[] bytes) {
    return (bytes[11] & REGIONIDBIT) == 0;
  }

  private static ZoneOffset extractOffset(byte[] bytes) {
    int hours = bytes[11] - 20;
    int minutes = bytes[12] - 60;
    if (hours == 0 && minutes == 0) {
      return ZoneOffset.UTC;
    }
    return ZoneOffset.ofHoursMinutes(hours, minutes);
  }

  private static ZoneId extractZoneId(byte[] bytes) {
    // high order bits
    int regionCode = (bytes[11] & 0b1111111) << 6;
    // low order bits
    regionCode += (bytes[12] & 0b11111100) >> 2;
    String regionName = ZONEIDMAP.getRegion(regionCode);
    return ZoneId.of(regionName);
  }

  private static boolean isValidRegionCode(int regionCode) {
    return regionCode != INV_ZONEID;
  }

  private static byte[] newTimestamptzBuffer() {
    return new byte[SIZE_TIMESTAMPTZ];
  }

  private static byte[] newTimestampBuffer() {
    return new byte[SIZE_TIMESTAMP];
  }

  private static void writeDateTime(byte[] bytes, LocalDateTime utc) {
    int year = utc.getYear();
    bytes[0] = (byte) (year / 100 + 100);
    bytes[1] = (byte) (year % 100 + 100);

    bytes[2] = (byte) utc.getMonthValue();
    bytes[3] = (byte) utc.getDayOfMonth();
    bytes[4] = (byte) (utc.getHour() + 1);
    bytes[5] = (byte) (utc.getMinute() + 1);
    bytes[6] = (byte) (utc.getSecond() + 1);

    int nano = utc.getNano();
    bytes[7] = (byte) (nano >> 24);
    bytes[8] = (byte) (nano >> 16 & 0xFF);
    bytes[9] = (byte) (nano >> 8 & 0xFF);
    bytes[10] = (byte) (nano & 0xFF);
  }

  private static void writeZoneOffset(byte[] bytes, ZoneOffset offset) {
    int totalMinutes = offset.getTotalSeconds() / 60;
    bytes[11] = (byte) ((totalMinutes / 60) + OFFSET_HOUR);
    bytes[12] = (byte) ((totalMinutes % 60) + OFFSET_MINUTE);
  }

  private static void writeZoneId(byte[] bytes, int regionCode) {
    bytes[11] = (byte) (REGIONIDBIT | (regionCode & 0b1111111000000) >>> 6);
    bytes[12] = (byte) ((regionCode & 0b111111) << 2);
  }

  private TimestamptzConverter() {
    throw new AssertionError("not instantiable");
  }

}
