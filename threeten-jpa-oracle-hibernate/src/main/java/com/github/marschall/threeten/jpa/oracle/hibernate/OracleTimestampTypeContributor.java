package com.github.marschall.threeten.jpa.oracle.hibernate;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

/**
 * TypeContributor for adding Oracle TIMESTAMP specific Type implementations.
 */
public class OracleTimestampTypeContributor implements TypeContributor {

  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    // register the Hibernate type mappings
    typeContributions.contributeType(OracleLocalDateTimeType.INSTANCE, OracleLocalDateTimeType.NAME);
  }

}
