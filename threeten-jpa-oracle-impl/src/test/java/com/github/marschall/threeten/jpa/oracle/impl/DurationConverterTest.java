package com.github.marschall.threeten.jpa.oracle.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Duration;

import org.junit.Test;

public class DurationConverterTest {

  @Test
  public void convertAndBackAllFields() {
    Duration duration = Duration.parse("P" + "2D" + "T" + "3H" + "4M" + "20.345S");
    Duration converted = IntervalConverter.intervaldsToDuration(IntervalConverter.durationToIntervalds(duration));
    assertEquals(duration, converted);
  }

  @Test
  public void nanosOnly() {
    Duration duration = Duration.parse("P" + "T" + "0.123456789S");
    Duration converted = IntervalConverter.intervaldsToDuration(IntervalConverter.durationToIntervalds(duration));
    assertEquals(duration, converted);
  }

  @Test
  public void secondsOnly() {
    Duration duration = Duration.parse("P" + "T" + "20S");
    Duration converted = IntervalConverter.intervaldsToDuration(IntervalConverter.durationToIntervalds(duration));
    assertEquals(duration, converted);
  }

  @Test
  public void hoursOnly() {
    Duration duration = Duration.parse("P" + "T" + "3H");
    Duration converted = IntervalConverter.intervaldsToDuration(IntervalConverter.durationToIntervalds(duration));
    assertEquals(duration, converted);
  }

  @Test
  public void daysOnly() {
    Duration duration = Duration.parse("P" + "2D");
    Duration converted = IntervalConverter.intervaldsToDuration(IntervalConverter.durationToIntervalds(duration));
    assertEquals(duration, converted);
  }

  @Test
  public void secondsAndNanos() {
    Duration duration = Duration.parse("P" + "T" + "20.345S");
    Duration converted = IntervalConverter.intervaldsToDuration(IntervalConverter.durationToIntervalds(duration));
    assertEquals(duration, converted);
  }

  @Test
  public void secondsAndMinutes() {
    Duration duration = Duration.parse("P" + "T" + "4M" + "20S");
    Duration converted = IntervalConverter.intervaldsToDuration(IntervalConverter.durationToIntervalds(duration));
    assertEquals(duration, converted);
  }

  @Test
  public void secondsAndMinutesAndHours() {
    Duration duration = Duration.parse("P" + "T" + "3H" + "4M" + "20S");
    Duration converted = IntervalConverter.intervaldsToDuration(IntervalConverter.durationToIntervalds(duration));
    assertEquals(duration, converted);
  }

  @Test
  public void negative() {
    Duration duration = Duration.parse("-P" + "2D");
    assertTrue(duration.isNegative());
    try {
      IntervalConverter.durationToIntervalds(duration);
      fail("negative durations not allowed");
    } catch (IllegalArgumentException e) {
      // should reach here
    }
  }

}
