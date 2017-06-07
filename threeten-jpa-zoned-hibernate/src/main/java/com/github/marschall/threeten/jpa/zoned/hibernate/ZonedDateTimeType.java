package com.github.marschall.threeten.jpa.zoned.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.OffsetDateTimeType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

public class ZonedDateTimeType implements CompositeUserType {

  /**
   * Singleton access.
   */
  public static final CompositeUserType INSTANCE = new ZonedDateTimeType();

  /**
   * Name of the type.
   */
  public static final String NAME = "ZonedDateTimeType";

  private static final int TIMESTAMP_UTC_INDEX = 0;
  private static final int ZONE_ID_INDEX = 1;

  private static final Type[] PROPERTY_TYPES = new Type[] {OffsetDateTimeType.INSTANCE, StringType.INSTANCE};

  private static final String[] PROPERTY_NAMES = new String[] {"timestamp_utc", "zoneid"};

  @Override
  public String[] getPropertyNames() {
    return PROPERTY_NAMES;
  }

  @Override
  public Type[] getPropertyTypes() {
    return PROPERTY_TYPES;
  }

  @Override
  public Object getPropertyValue(Object component, int property) throws HibernateException {
    if (component == null) {
      return null;
    }
    ZonedDateTime value = (ZonedDateTime) component;
    switch (property) {
      case TIMESTAMP_UTC_INDEX:
        return toUtc(value);
      case ZONE_ID_INDEX:
        return getZoneId(value);
      default:
        throw new HibernateException("unknown property index: " + property);
    }
  }

  @Override
  public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
    throw new HibernateException("values are immutable");
  }

  @Override
  public Class<?> returnedClass() {
    return ZonedDateTimeType.class;
  }

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
  public Serializable disassemble(Object value, SharedSessionContractImplementor session) {
    return (Serializable) value;
  }

  @Override
  public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner) {
    return cached;
  }

  @Override
  public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner) {
    return original;
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      st.setNull(index + TIMESTAMP_UTC_INDEX, Types.TIME_WITH_TIMEZONE);
      st.setNull(index + ZONE_ID_INDEX, Types.VARCHAR);
      return;
    }

    ZonedDateTime zonedDateTime = (ZonedDateTime) value;
    st.setObject(index + TIMESTAMP_UTC_INDEX, toUtc(zonedDateTime));
    st.setString(index + ZONE_ID_INDEX, getZoneId(zonedDateTime));
  }

  private static String getZoneId(ZonedDateTime zonedDateTime) {
    return zonedDateTime.getZone().getId();
  }

  private static OffsetDateTime toUtc(ZonedDateTime zonedDateTime) {
    return zonedDateTime.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
    OffsetDateTime dateTime = rs.getObject(names[TIMESTAMP_UTC_INDEX], OffsetDateTime.class);
    String zoneIdName = rs.getString(names[ZONE_ID_INDEX]);

    if (dateTime == null || zoneIdName == null) {
      return null;
    }
    ZoneId zoneId;
    try {
      zoneId = ZoneId.of(zoneIdName);
    } catch (DateTimeException e) {
      throw new HibernateException("invalid zone id: " + zoneIdName, e);
    }
    return dateTime.atZoneSameInstant(zoneId);
  }

}
