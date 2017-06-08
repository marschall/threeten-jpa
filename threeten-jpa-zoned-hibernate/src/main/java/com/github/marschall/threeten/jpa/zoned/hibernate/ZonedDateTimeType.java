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

/**
 * Emulates {@link ZonedDateTime} support by storing a timestamp in a
 * <code>TIMESTAMP WITH TIME ZONE</code> column named "timestamp_utc"
 * and a time zone id in a <code>VARCHAR</code> column named "zoneid".
 *
 * <p>The timestamp value is always converted to UTC.</p>
 *
 * <h2>Ordering</h2>
 * Ordering on the composite column always done by the timestamp value
 * first and the zone id second. However ordering by can also be done
 * by the individual properties with JPQL
 * <pre><code>SELECT t
 * FROM MyEntity t
 * ORDER BY t.zonedDateTimeColumn.zoneid</code></pre>
 *
 * <h2>Querying</h2>
 * When querying the composite attribute the following limitations apply.
 * <ul>
 *  <li>Equality comparisons (=) always consider the name of the time zone.</li>
 *  <li>Non-equality comparisons (!=) always consider the name of the time zone.</li>
 *  <li>Other comparisons (&lt;, &lt;=, &gt;=, &gt;) are not supported.</li>
 * </ul>
 * However the individual properties can be queried in JPQL.
 * <pre><code>SELECT t
 * FROM MyEntity t
 * WHERE t.zonedDateTimeColumn.timestamp_utc &lt; :value</code></pre>
 * <pre><code>SELECT t
 * FROM MyEntity t
 * WHERE t.zonedDateTimeColumn.zoneid = :zoneid</code></pre>
 * This does not work with Criteria API.
 *
 * @see <a href="https://hibernate.atlassian.net/browse/HHH-7302">HHH-7302</a>
 */
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
    return ZonedDateTime.class;
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
