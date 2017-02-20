package com.github.marschall.threeten.jpa.oracle.hibernate;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.usertype.UserType;

/**
 * Abstract base class for all JSR-310 types.
 */
abstract class AbstractThreeTenType implements UserType {

  @Override
  public boolean equals(Object x, Object y) {
    return Objects.equals(x, y);
  }

  @Override
  public int hashCode(Object x) {
    return Objects.hashCode(x);
  }

  @Override
  public Object deepCopy(Object value) {
    return value;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(Object value) {
    return (Serializable) value;
  }

  @Override
  public Object assemble(Serializable cached, Object owner) {
    return cached;
  }

  @Override
  public Object replace(Object original, Object target, Object owner) {
    return original;
  }

}