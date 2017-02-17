package com.github.marschall.threeten.jpa.oracle.impl;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.Ignore;
import org.junit.Test;

public class DurationConverterTest {

  @Test
  @Ignore("broken")
  public void convertAndBack() {
    Duration duration = Duration.parse("P" + "2DT" + "3H" + "4M" + "20.345S");
    Duration converted = IntervalConverter.intervaldsToDuration(IntervalConverter.durationToIntervalds(duration));
    assertEquals(duration, converted);
  }

}
