package com.github.marschall.threeten.jpa.oracle.h2;

import static org.junit.Assert.*;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoField;

import org.junit.Test;

public class TimestampWithTimeZoneConverterTest {

  @Test
  public void testNull() {
    assertNull(TimestampWithTimeZoneConverter.offsetDateTimeToTimestampWithTimeZone(null));
    assertNull(TimestampWithTimeZoneConverter.timestampWithTimeZoneToOffsetDateTime(null));
  }

  @Test
  public void onlyNano() {
    OffsetDateTime original = OffsetDateTime.parse("2016-09-12T12:16:54.000000001Z");
    assertEquals(1L, original.getNano());
    assertEquals(0L, original.get(ChronoField.MILLI_OF_SECOND));

    OffsetDateTime actual = TimestampWithTimeZoneConverter.timestampWithTimeZoneToOffsetDateTime(TimestampWithTimeZoneConverter.offsetDateTimeToTimestampWithTimeZone(original));
    assertEquals(original, actual);
  }

  @Test
  public void oneNanoOneMilli() {
    OffsetDateTime original = OffsetDateTime.parse("2016-09-12T12:16:54.001000001Z");
    assertEquals(1000001L, original.getNano());
    assertEquals(1L, original.get(ChronoField.MILLI_OF_SECOND));

    OffsetDateTime actual = TimestampWithTimeZoneConverter.timestampWithTimeZoneToOffsetDateTime(TimestampWithTimeZoneConverter.offsetDateTimeToTimestampWithTimeZone(original));
    assertEquals(original, actual);
  }

}
