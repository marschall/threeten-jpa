package com.github.marschall.threeten.jpa;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
    H2Configuration.class,
    TransactionManagerConfiguration.class,
    EclipseLinkH2Configuration.class})
public class EclipseLinkH2Test extends AbstractConverterTest {

}
