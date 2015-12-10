package com.github.marschall.threeten.jpa.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

public class HibernateZonedDateTimeType extends AbstraceThreeTenType {

  @Override
  public int[] sqlTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<?> returnedClass() {
    return ZonedDateTime.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    // TODO Auto-generated method stub
    rs.getString(names[0]);
    return null;
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
    st.setString(index, null);
    // TODO Auto-generated method stub
  }

}
