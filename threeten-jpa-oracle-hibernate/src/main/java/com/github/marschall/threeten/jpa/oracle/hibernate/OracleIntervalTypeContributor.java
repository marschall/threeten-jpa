package com.github.marschall.threeten.jpa.oracle.hibernate;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

/**
 * TypeContributor for adding Oracle INTERVALYM and INTERVALDS specific Type implementations.
 */
public class OracleIntervalTypeContributor implements TypeContributor {

  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    // register the Hibernate type mappings
    typeContributions.contributeType(OracleDurationType.INSTANCE, OracleDurationType.NAME);
    typeContributions.contributeType(OraclePeriodType.INSTANCE, OraclePeriodType.NAME);
    typeContributions.contributeType(OracleJdbc42DurationType.INSTANCE, OracleJdbc42DurationType.NAME);
    typeContributions.contributeType(OracleJdbc42PeriodType.INSTANCE, OracleJdbc42PeriodType.NAME);
  }

}
