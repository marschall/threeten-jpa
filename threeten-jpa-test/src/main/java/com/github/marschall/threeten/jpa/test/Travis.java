package com.github.marschall.threeten.jpa.test;

public final class Travis {

  private Travis() {
    throw new AssertionError("not instantiable");
  }

  public static boolean isTravis() {
    return System.getenv().getOrDefault("TRAVIS", "false").equals("true");
  }

}
