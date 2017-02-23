package com.github.marschall.threeten.jpa.oracle.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.Period;
import java.time.ZonedDateTime;

import org.junit.Test;

public class PeriodConverterTest {

  @Test
  public void convertAndBack() {
    Period period = Period.of(100, 10, 0);
    Period converted = IntervalConverter.intervalymToPeriod(IntervalConverter.periodToIntervalym(period));
    assertEquals(period, converted);
  }

  @Test
  public void negative() {
    Period period = Period.of(-100, 10, 0);
    try {
      IntervalConverter.periodToIntervalym(period);
      fail("negative periods not allowed");
    } catch (IllegalArgumentException e) {
      // should reach here
    }
  }

  @Test
  public void day() {
    Period period = Period.ofDays(1);
    try {
      IntervalConverter.periodToIntervalym(period);
      fail("days not allowed");
    } catch (IllegalArgumentException e) {
      // should reach here
    }
  }

  @Test
  public void truncation() {
    ZonedDateTime zoned = ZonedDateTime.parse("2017-01-31T11:52:47.971+01:00[Europe/Zurich]");
    assertEquals(ZonedDateTime.parse("2017-02-28T11:52:47.971+01:00[Europe/Zurich]"), zoned.plusMonths(1L));

    zoned = ZonedDateTime.parse("2016-02-29T11:52:47.971+01:00[Europe/Zurich]");
    assertEquals(ZonedDateTime.parse("2017-02-28T11:52:47.971+01:00[Europe/Zurich]"), zoned.plusMonths(12L));

    zoned = ZonedDateTime.parse("2016-02-29T11:52:47.971+01:00[Europe/Zurich]");
    assertEquals(ZonedDateTime.parse("2017-02-28T11:52:47.971+01:00[Europe/Zurich]"), zoned.plusYears(1L));
  }

}
