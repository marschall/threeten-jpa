package com.github.marschall.threeten.jpa.oracle;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import oracle.sql.TIMESTAMPTZ;

import org.junit.Before;
import org.junit.Test;

public class OracleZonedDateTimeConverterTest {

  private OracleZonedDateTimeConverter converter;

  @Before
  public void setUp() {
    this.converter = new OracleZonedDateTimeConverter();
  }

  @Test
  public void convertToDatabaseColumnPositiveOffset() {
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(2, 0);
    LocalDate date = LocalDate.of(1997, 1, 31);
    LocalTime time = LocalTime.of(9, 26, 56, 660000000);
    ZonedDateTime attribute = ZonedDateTime.of(date, time, offset);

    TIMESTAMPTZ timestamptz = this.converter.convertToDatabaseColumn(attribute);
    byte[] actual = timestamptz.toBytes();
    byte[] expected = new byte[]{119, -59, 1, 31, 8, 27, 57, 39, 86, -51, 0, 22, 60};
    assertArrayEquals(expected, actual);
  }

  @Test
  public void convertToDatabaseColumnNegativeOffset() {
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(-2, -30);
    LocalDate date = LocalDate.of(1997, 1, 31);
    LocalTime time = LocalTime.of(9, 26, 56, 660000000);
    ZonedDateTime attribute = ZonedDateTime.of(date, time, offset);

    TIMESTAMPTZ timestamptz = this.converter.convertToDatabaseColumn(attribute);
    byte[] actual = timestamptz.toBytes();
    byte[] expected = new byte[]{119, -59, 1, 31, 12, 57, 57, 39, 86, -51, 0, 18, 30};
    assertArrayEquals(expected, actual);
  }

  @Test
  public void convertToEntityAttributePositiveOffset() {
    byte[] timestamptz = new byte[]{119, -59, 1, 31, 8, 27, 57, 39, 86, -51, 0, 22, 60};
    TIMESTAMPTZ dbData = new TIMESTAMPTZ(timestamptz);

    ZoneOffset offset = ZoneOffset.ofHoursMinutes(2, 0);
    LocalDate date = LocalDate.of(1997, 1, 31);
    LocalTime time = LocalTime.of(9, 26, 56, 660000000);
    ZonedDateTime exptected = ZonedDateTime.of(date, time, offset);
    ZonedDateTime actual = this.converter.convertToEntityAttribute(dbData);
    assertEquals(exptected, actual);
  }

  @Test
  public void convertToEntityAttributeNegativeOffset() {
    byte[] timestamptz = new byte[]{119, -59, 1, 31, 12, 57, 57, 39, 86, -51, 0, 18, 30};
    TIMESTAMPTZ dbData = new TIMESTAMPTZ(timestamptz);

    ZoneOffset offset = ZoneOffset.ofHoursMinutes(-2, -30);
    LocalDate date = LocalDate.of(1997, 1, 31);
    LocalTime time = LocalTime.of(9, 26, 56, 660000000);
    ZonedDateTime exptected = ZonedDateTime.of(date, time, offset);
    ZonedDateTime actual = this.converter.convertToEntityAttribute(dbData);
    assertEquals(exptected, actual);
  }

  @Test
  public void convertToEntityAttributeNamedZone() {
    byte[] timestamptz = new byte[]{119, -57, 1, 15, 17, 1, 1, 0, 0, 0, 0, -119, -100};
    TIMESTAMPTZ dbData = new TIMESTAMPTZ(timestamptz);

    ZoneId zoneId = ZoneId.of("US/Pacific");
    LocalDate date = LocalDate.of(1999, 1, 15);
    LocalTime time = LocalTime.of(8, 0, 0);
    ZonedDateTime exptected = ZonedDateTime.of(date, time, zoneId);
    ZonedDateTime actual = this.converter.convertToEntityAttribute(dbData);
    assertEquals(exptected, actual);
  }

  @Test
  public void convertToEntityAttributeNamedZoneEuropeZurich() {
    int[] expected = new int[]{120, 115, 10, 24, 24, 46, 1, 0, 0, 0, 0, 134, 88};
    byte[] data = convert(expected);
    ZonedDateTime zonedDateTime = this.converter.convertToEntityAttribute(new TIMESTAMPTZ(data));
    assertNotNull(zonedDateTime);
  }

  @Test
  public void convertToDatabaseColumnEuropeZurich() {
    ZoneId zoneId = ZoneId.of("Europe/Zurich");
    ZonedDateTime zonedDateTime = LocalDateTime.of(LocalDate.of(2015, 10, 25), LocalTime.of(1, 45)).atZone(zoneId);
    byte[] bytes = this.converter.convertToDatabaseColumn(zonedDateTime).toBytes();
    int[] expected = new int[]{120, 115, 10, 24, 24, 46, 1, 0, 0, 0, 0, 134, 88};
    assertEquals(expected.length, bytes.length);
    for (int i = 0; i < expected.length; i++) {
      int expectedValue = expected[i];
      int actualValue = bytes[i] & 0xFF;
      assertEquals(expectedValue, actualValue);
    }
  }

  private static byte[] convert(int[] original) {
    byte[] result = new byte[original.length];
    for (int i = 0; i < original.length; i++) {
      result[i] = (byte) original[i];
    }
    return result;
  }

}
