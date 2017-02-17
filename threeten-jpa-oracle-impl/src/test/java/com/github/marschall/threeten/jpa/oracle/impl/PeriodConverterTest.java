package com.github.marschall.threeten.jpa.oracle.impl;

import static org.junit.Assert.assertEquals;

import java.time.Period;

import org.junit.Test;

public class PeriodConverterTest {

  @Test
  public void convertAndBack() {
    Period period = Period.of(100, 10, 0);
    Period converted = IntervalConverter.intervalymToPeriod(IntervalConverter.periodToIntervalym(period));
    assertEquals(period, converted);
  }

}
