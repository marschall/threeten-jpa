package com.github.marschall.threeten.jpa.mssql.hibernate;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

/**
 * TypeContributor for adding SQL Server DATETIMEOFFSET specific Type implementations.
 */
public class MssqlOffsetDateTimeTypeContributor implements TypeContributor {

  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    // register the Hibernate type mappings
    typeContributions.contributeType(MssqlOffsetDateTimeType.INSTANCE, MssqlOffsetDateTimeType.NAME);
  }

}
