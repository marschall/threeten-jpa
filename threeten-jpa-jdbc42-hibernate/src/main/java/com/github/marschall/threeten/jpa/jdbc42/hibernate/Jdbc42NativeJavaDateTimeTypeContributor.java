package com.github.marschall.threeten.jpa.jdbc42.hibernate;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

/**
 * TypeContributor for adding JDBC 4.2 based Type implementations for {@code TIMESTAMP WITH TIME ZONE}.
 */
public class Jdbc42NativeJavaDateTimeTypeContributor implements TypeContributor {

  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    // register the Hibernate type mappings
    typeContributions.contributeType(Jdbc42ZonedDateTimeType.INSTANCE, Jdbc42ZonedDateTimeType.NAME);
    typeContributions.contributeType(Jdbc42OffsetDateTimeType.INSTANCE, Jdbc42OffsetDateTimeType.NAME);

    typeContributions.contributeType(Jdbc42LocalDateTimeType.INSTANCE, Jdbc42LocalDateTimeType.NAME);
    typeContributions.contributeType(Jdbc42LocalDateType.INSTANCE, Jdbc42LocalDateType.NAME);
    typeContributions.contributeType(Jdbc42LocalTimeType.INSTANCE, Jdbc42LocalTimeType.NAME);
  }

}
