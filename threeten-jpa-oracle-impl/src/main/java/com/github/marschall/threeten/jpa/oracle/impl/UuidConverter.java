package com.github.marschall.threeten.jpa.oracle.impl;

import java.util.UUID;

final class UuidConverter {

  static byte[] toRaw(UUID uuid) {
    long mostSignificantBits = uuid.getMostSignificantBits();
    long leastSignificantBits = uuid.getLeastSignificantBits();
    return new byte[] {
        (byte) (mostSignificantBits >>> 56),
        (byte) (mostSignificantBits >>> 48),
        (byte) (mostSignificantBits >>> 40),
        (byte) (mostSignificantBits >>> 32),
        (byte) (mostSignificantBits >>> 24),
        (byte) (mostSignificantBits >>> 16),
        (byte) (mostSignificantBits >>> 8),
        (byte) (mostSignificantBits),
        (byte) (leastSignificantBits >>> 56),
        (byte) (leastSignificantBits >>> 48),
        (byte) (leastSignificantBits >>> 40),
        (byte) (leastSignificantBits >>> 32),
        (byte) (leastSignificantBits >>> 24),
        (byte) (leastSignificantBits >>> 16),
        (byte) (leastSignificantBits >>> 8),
        (byte) (leastSignificantBits),
    };
  }

  static UUID fromRaw(byte[] raw) {
    if (raw.length != 16) {
      throw new IllegalArgumentException("raw byte array must of of length 16");
    }
    // Byte.toUnsignedLong
    long mostSignificantBits =  ((((long) raw[0]) & 0xFFL) << 56)
                              | ((((long) raw[1]) & 0xFFL) << 48)
                              | ((((long) raw[2]) & 0xFFL) << 40)
                              | ((((long) raw[3]) & 0xFFL) << 32)
                              | ((((long) raw[4]) & 0xFFL) << 24)
                              | ((((long) raw[5]) & 0xFFL) << 16)
                              | ((((long) raw[6]) & 0xFFL) << 8)
                              | ((((long) raw[7]) & 0xFFL));
    long leastSignificantBits = ((((long) raw[8])  & 0xFFL)  << 56)
                              | ((((long) raw[9])  & 0xFFL) << 48)
                              | ((((long) raw[10]) & 0xFFL) << 40)
                              | ((((long) raw[11]) & 0xFFL) << 32)
                              | ((((long) raw[12]) & 0xFFL) << 24)
                              | ((((long) raw[13]) & 0xFFL) << 16)
                              | ((((long) raw[14]) & 0xFFL) << 8)
                              | ((((long) raw[15]) & 0xFFL));
    return new UUID(mostSignificantBits, leastSignificantBits);
  }

}
