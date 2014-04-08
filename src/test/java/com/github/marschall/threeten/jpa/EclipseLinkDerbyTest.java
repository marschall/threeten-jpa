package com.github.marschall.threeten.jpa;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {DerbyConfiguration.class, EclipseLinkDerbyConfiguration.class})
public class EclipseLinkDerbyTest extends AbstractConverterTest {

}
