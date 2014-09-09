package com.github.marschall.threeten.jpa.oracle;

import static java.lang.Byte.toUnsignedInt;
import static java.time.ZoneOffset.UTC;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import oracle.sql.ZONEIDMAP;

final class OracleTimeConverter {

  final static int INV_ZONEID = -1;

  // magic
  private static int SIZE_TIMESTAMPTZ = 13;
  private static int OFFSET_HOUR = 20;
  private static int OFFSET_MINUTE = 60;
  private static byte REGIONIDBIT = (byte) 0b1000_0000; // -128

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

  private OracleTimeConverter() {
    throw new AssertionError("not instantiable");
  }

  static OffsetDateTime extractUtc(byte[] bytes) {
    int year = ((toUnsignedInt(bytes[0]) - 100) * 100) + (toUnsignedInt(bytes[1]) - 100);
    int month = bytes[2];
    int dayOfMonth = bytes[3];
    int hour = bytes[4] -1;
    int minute = bytes[5] - 1;
    int second = bytes[6] - 1;
    int nanoOfSecond = toUnsignedInt(bytes[7]) << 24
        | toUnsignedInt(bytes[8]) << 16
        | toUnsignedInt(bytes[9]) << 8
        | toUnsignedInt(bytes[10]);
    return OffsetDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, UTC);
  }

  static boolean isFixedOffset(byte[] bytes) {
    return (bytes[11] & REGIONIDBIT) == 0;
  }

  static ZoneOffset extractOffset(byte[] bytes) {
    return ZoneOffset.ofHoursMinutes(bytes[11] - 20, bytes[12] - 60);
  }

  static ZoneId extractZoneId(byte[] bytes) {
    // high order bits
    int regionCode = (bytes[11] & 0b1111111) << 6;
    // low order bits
    regionCode += (bytes[12] & 0b11111100) >> 2;
    String regionName = ZONEIDMAP.getRegion(regionCode);
    return ZoneId.of(regionName);
  }

  static boolean isValidRegionCode(int regionCode) {
    return regionCode != INV_ZONEID;
  }

  static byte[] newBuffer() {
    return new byte[SIZE_TIMESTAMPTZ];
  }

  static void writeDateTime(byte[] bytes, ZonedDateTime utc) {
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

  static void writeZoneOffset(byte[] bytes, ZoneOffset offset) {
    int totalMinutes = offset.getTotalSeconds() / 60;
    bytes[11] = (byte) ((totalMinutes / 60) + OFFSET_HOUR);
    bytes[12] = (byte) ((totalMinutes % 60) + OFFSET_MINUTE);
  }
  
  static void writeZoneId(byte[] bytes, int regionCode) {
    bytes[11] = (byte) (REGIONIDBIT | (byte) (regionCode & 0b11111100000) >>> 6);
    bytes[12] = (byte) ((regionCode & 0b11111) << 2);
  }

}
