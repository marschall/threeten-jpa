package com.github.marschall.threeten.jpa.oracle.h2;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

/**
 * TypeContributor for adding H2 TIMESTAMP WITH TIME ZONE specific Type implementations
 */
public class H2TimestampWithTimeZoneTypeContributor implements TypeContributor {

  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    // register the Hibernate type mappings
    typeContributions.contributeType(H2OffsetDateTimeType.INSTANCE, H2OffsetDateTimeType.NAME);
  }

}
