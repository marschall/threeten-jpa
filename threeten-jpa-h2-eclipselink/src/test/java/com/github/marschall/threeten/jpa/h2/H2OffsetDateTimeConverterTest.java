package com.github.marschall.threeten.jpa.h2;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.h2.api.TimestampWithTimeZone;
import org.junit.Before;
import org.junit.Test;

public class H2OffsetDateTimeConverterTest {

  private H2OffsetDateTimeConverter converter;

  @Before
  public void setUp() {
    this.converter = new H2OffsetDateTimeConverter();
  }

  @Test
  public void convertToDatabaseColumnPositiveOffset() {
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(2, 0);
    LocalDate date = LocalDate.of(1997, 1, 31);
    LocalTime time = LocalTime.of(9, 26, 56, 660000000);
    OffsetDateTime attribute = OffsetDateTime.of(date, time, offset);

    TimestampWithTimeZone dbData = this.converter.convertToDatabaseColumn(attribute);

    assertEquals("1997-01-31 09:26:56.66+02", dbData.toString());
    assertEquals(attribute, this.converter.convertToEntityAttribute(dbData));
  }

  @Test
  public void convertToDatabaseColumnNegativeOffset() {
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(-2, -30);
    LocalDate date = LocalDate.of(1997, 1, 31);
    LocalTime time = LocalTime.of(9, 26, 56, 660000000);
    OffsetDateTime attribute = OffsetDateTime.of(date, time, offset);

    TimestampWithTimeZone dbData = this.converter.convertToDatabaseColumn(attribute);

    assertEquals("1997-01-31 09:26:56.66-02:30", dbData.toString());
    assertEquals(attribute, this.converter.convertToEntityAttribute(dbData));
  }

}
