package com.github.marschall.threeten.jpa.zoned.hibernate;

import java.time.ZoneId;

import org.junit.Test;

public class CompositeUserTypeTest {

  @Test
  public void zoneIdLength() {
    String longest = null;
    for (String zoneId : ZoneId.getAvailableZoneIds()) {
      if (longest == null || zoneId.length() > longest.length()) {
        longest = zoneId;
      }
    }
    System.out.println(longest + ": " + longest.length());
  }

}
