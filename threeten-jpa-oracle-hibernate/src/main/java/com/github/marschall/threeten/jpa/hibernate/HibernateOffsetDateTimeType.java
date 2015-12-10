package com.github.marschall.threeten.jpa.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

public class HibernateOffsetDateTimeType extends AbstraceThreeTenType {

  @Override
  public int[] sqlTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<?> returnedClass() {
    return OffsetDateTime.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
          throws HibernateException, SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
          throws HibernateException, SQLException {
    // TODO Auto-generated method stub

  }

}
