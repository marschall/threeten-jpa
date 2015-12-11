package com.github.marschall.threeten.jpa.hibernate;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

public class OracleTimestamptzTypeContributor implements TypeContributor {

  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    typeContributions.contributeType(OracleZonedDateTimeType.INSTANCE, OracleZonedDateTimeType.class.getSimpleName());
    typeContributions.contributeType(OracleOffsetDateTimeType.INSTANCE, OracleOffsetDateTimeType.class.getSimpleName());
  }

}
