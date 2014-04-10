package com.github.marschall.threeten.jpa;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
    HsqlConfiguration.class,
    TransactionManagerConfiguration.class,
    EclipseLinkHsqlConfiguration.class})
public class EclipseLinkHsqlTest extends AbstractConverterTest {

}
