package com.github.marschall.threeten.jpa;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {H2Configuration.class, HibernateH2Configuration.class})
public class HibernateH2Test extends AbstractConverterTest {

}
