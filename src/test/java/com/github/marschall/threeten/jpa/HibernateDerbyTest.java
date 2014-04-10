package com.github.marschall.threeten.jpa;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
    DerbyConfiguration.class,
    TransactionManagerConfiguration.class,
    HibernateDerbyConfiguration.class})
public class HibernateDerbyTest extends AbstractConverterTest {

}
