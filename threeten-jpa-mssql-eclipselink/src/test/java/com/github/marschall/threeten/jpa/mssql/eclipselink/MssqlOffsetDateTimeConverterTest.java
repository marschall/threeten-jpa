package com.github.marschall.threeten.jpa.mssql.eclipselink;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.Before;
import org.junit.Test;

import microsoft.sql.DateTimeOffset;

public class MssqlOffsetDateTimeConverterTest {

  private MssqlOffsetDateTimeConverter converter;

  @Before
  public void setUp() {
    this.converter = new MssqlOffsetDateTimeConverter();
  }

  @Test
  public void convertToDatabaseColumnPositiveOffset() {
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(2, 0);
    LocalDate date = LocalDate.of(1997, 1, 31);
    LocalTime time = LocalTime.of(9, 26, 56, 123456700);
    OffsetDateTime attribute = OffsetDateTime.of(date, time, offset);

    DateTimeOffset dbData = this.converter.convertToDatabaseColumn(attribute);

    assertEquals("1997-01-31 09:26:56.1234567 +02:00", dbData.toString());
    assertEquals(attribute, this.converter.convertToEntityAttribute(dbData));
  }

  @Test
  public void convertToDatabaseColumnNegativeOffset() {
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(-2, -30);
    LocalDate date = LocalDate.of(1997, 1, 31);
    LocalTime time = LocalTime.of(9, 26, 56, 123456700);
    OffsetDateTime attribute = OffsetDateTime.of(date, time, offset);

    DateTimeOffset dbData = this.converter.convertToDatabaseColumn(attribute);

    assertEquals("1997-01-31 09:26:56.1234567 -02:30", dbData.toString());
    assertEquals(attribute, this.converter.convertToEntityAttribute(dbData));
  }

}
