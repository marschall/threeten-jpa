package com.github.marschall.threeten.jpa.zoned.hibernate;

import java.time.ZonedDateTime;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

/**
 * TypeContributor for emulating support for {@link ZonedDateTime}.
 */
public class ZonedDateTimeTypeContributor implements TypeContributor {

  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    // register the Hibernate type mappings
    typeContributions.contributeType(ZonedDateTimeType.INSTANCE, ZonedDateTimeType.NAME);
  }

}
