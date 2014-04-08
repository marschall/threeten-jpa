package com.github.marschall.threeten.jpa;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {DerbyConfiguration.class, HibernateConfiguration.class})
public class HibernateConveterTest extends AbstractConverterTest {

}
