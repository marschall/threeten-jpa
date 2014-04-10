package com.github.marschall.threeten.jpa;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

@Ignore("Open JPA does not yet support JPA 2.1")
@ContextConfiguration(classes = {
    DerbyConfiguration.class,
    TransactionManagerConfiguration.class,
    OpenJpaConfiguration.class})
public class OpenJpaDerbyConveterTest extends AbstractConverterTest {

}
