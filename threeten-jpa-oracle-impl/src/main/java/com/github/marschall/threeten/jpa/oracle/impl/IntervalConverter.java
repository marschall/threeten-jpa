package com.github.marschall.threeten.jpa.oracle.impl;

import static java.lang.Byte.toUnsignedInt;
import static java.lang.Math.toIntExact;

import java.time.Duration;
import java.time.Period;

import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;

/**
 * Converts between {@link INTERVALYM}/{@link Period} and
 * {@link INTERVALDS}/{@link Duration} and back.
 */
public final class IntervalConverter {

  private static final int SIZE_INTERVALYM = 5;
  private static final int SIZE_INTERVALDS = 11;

  private static final int HIGH_BIT_FLAG = 0x80000000;

  /**
   * Converts {@link INTERVALYM} to {@link Period}.
   *
   * @param dbData the data from the database to be converted, possibly {@code null}
   * @return the converted value, possibly {@code null}
   */
  public static Period intervalymToPeriod(INTERVALYM dbData) {
    if (dbData == null) {
      return null;
    }

    byte[] bytes = dbData.toBytes();
    int year = toUnsignedInt(bytes[0]) << 24
            | toUnsignedInt(bytes[1]) << 16
            | toUnsignedInt(bytes[2]) << 8
            | toUnsignedInt(bytes[3]);
    year ^= HIGH_BIT_FLAG;
    int month = toUnsignedInt(bytes[4]) - 60;
    return Period.of(year, month, 0);
  }

  /**
   * Converts {@link Period} to {@link INTERVALYM}.
   *
   * @param attribute the value to be converted, possibly {@code null}
   * @return the converted data, possibly {@code null}
   */
  public static INTERVALYM periodToIntervalym(Period attribute) {
    if (attribute == null) {
      return null;
    }
    if (attribute.getDays() != 0) {
      throw new IllegalArgumentException("days are not supported");
    }
    byte[] bytes = newIntervalymBuffer();

    int year = attribute.getYears() ^ HIGH_BIT_FLAG;
    bytes[0] = (byte) (year >> 24);
    bytes[1] = (byte) (year >> 16 & 0xFF);
    bytes[2] = (byte) (year >> 8 & 0xFF);
    bytes[3] = (byte) (year & 0xFF);

    int month = attribute.getMonths() + 60;
    bytes[4] = (byte) (month & 0xFF);

    return new INTERVALYM(bytes);
  }

  private static byte[] newIntervalymBuffer() {
    return new byte[SIZE_INTERVALYM];
  }

  /**
   * Converts {@link INTERVALDS} to {@link Duration}.
   *
   * @param dbData the data from the database to be converted, possibly {@code null}
   * @return the converted value, possibly {@code null}
   */
  public static Duration intervaldsToDuration(INTERVALDS dbData) {
    if (dbData == null) {
      return null;
    }

    byte[] bytes = dbData.toBytes();
    int day = toUnsignedInt(bytes[0]) << 24
            | toUnsignedInt(bytes[1]) << 16
            | toUnsignedInt(bytes[2]) << 8
            | toUnsignedInt(bytes[3]);
    day ^= HIGH_BIT_FLAG;
    int hour = toUnsignedInt(bytes[4]) - 60;
    int minute = toUnsignedInt(bytes[5]) - 60;
    int second = toUnsignedInt(bytes[6]) - 60;
    int nano = toUnsignedInt(bytes[7]) << 24
            | toUnsignedInt(bytes[8]) << 16
            | toUnsignedInt(bytes[9]) << 8
            | toUnsignedInt(bytes[10]);
    nano ^= HIGH_BIT_FLAG;
    return Duration.ofDays(day)
            .plusHours(hour)
            .plusMinutes(minute)
            .plusSeconds(second)
            .plusNanos(nano);
  }

  /**
   * Converts {@link Duration} to {@link INTERVALDS}.
   *
   * @param attribute the value to be converted, possibly {@code null}
   * @return the converted data, possibly {@code null}
   */
  public static INTERVALDS durationToIntervalds(Duration attribute) {
    if (attribute == null) {
      return null;
    }
    byte[] bytes = newIntervaldsBuffer();

    long totalSeconds = attribute.getSeconds();
    if (attribute.isNegative()) {
      //
      totalSeconds += 1L;
    }
    // computation happens in UTC
    // lead seconds are sort of an issue

    int day = toIntExact(totalSeconds / 24L / 60L / 60L);
    int hour = (int) ((totalSeconds / 60L / 60L) - (day * 24L));
    int minute = (int) ((totalSeconds / 60L) - (day * 24L * 60L) - (hour * 60L));
    int second = (int) (totalSeconds % 60);
    int nano;
    if (attribute.isNegative()) {
      // Java represents -10.1 seconds as
      //  -11 seconds
      //  +900000000 nanoseconds
      // Oracle wants -10.1 seconds as
      //  -10 seconds
      //  -100000000 nanoseconds
      nano = -(1_000_000_000 - attribute.getNano());
    } else {
      nano = attribute.getNano();
    }
    nano ^= HIGH_BIT_FLAG;
    day ^= HIGH_BIT_FLAG;

    hour += 60;
    minute += 60;
    second += 60;

    bytes[0] = (byte) (day >> 24);
    bytes[1] = (byte) (day >> 16 & 0xFF);
    bytes[2] = (byte) (day >> 8 & 0xFF);
    bytes[3] = (byte) (day & 0xFF);

    bytes[4] = (byte) (hour & 0xFF);
    bytes[5] = (byte) (minute & 0xFF);
    bytes[6] = (byte) (second & 0xFF);

    bytes[7] = (byte) (nano >> 24);
    bytes[8] = (byte) (nano >> 16 & 0xFF);
    bytes[9] = (byte) (nano >> 8 & 0xFF);
    bytes[10] = (byte) (nano & 0xFF);

    return new INTERVALDS(bytes);
  }

  private static byte[] newIntervaldsBuffer() {
    return new byte[SIZE_INTERVALDS];
  }

  private IntervalConverter() {
    throw new AssertionError("not instantiable");
  }

}
