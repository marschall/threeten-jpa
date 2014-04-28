package com.github.marschall.threeten.jpa.oracle;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import oracle.sql.TIMESTAMPTZ;

import org.junit.Before;
import org.junit.Test;

public class OracleOffsetDateTimeConverterTest {
  
  private OracleOffsetDateTimeConverter converter;

  @Before
  public void setUp() {
    this.converter = new OracleOffsetDateTimeConverter();
  }

  @Test
  public void convertToDatabaseColumn() {
  }
  
  @Test
  public void convertToEntityAttribute() {
    byte[] timestamptz = new byte[]{119, -59, 1, 31, 8, 27, 57, 39, 86, -51, 0, 22, 60};
    TIMESTAMPTZ dbData = new TIMESTAMPTZ(timestamptz);
    
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(2, 0);
    LocalDate date = LocalDate.of(1997, 1, 31);
    LocalTime time = LocalTime.of(9, 26, 56, 660000000);
    OffsetDateTime exptected = OffsetDateTime.of(date, time, offset);
    OffsetDateTime actual = this.converter.convertToEntityAttribute(dbData);
    assertEquals(exptected, actual);
    
  }

}
