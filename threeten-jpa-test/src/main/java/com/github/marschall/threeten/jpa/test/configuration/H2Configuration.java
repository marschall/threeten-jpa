package com.github.marschall.threeten.jpa.test.configuration;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

@Configuration
public class H2Configuration {

  private static final AtomicInteger COUNT = new AtomicInteger();
  
  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
         // the spring test context framework keeps application contexts
         // and thus databases around for the entire VM lifetime
         // so be have to create a unique name here to avoid sharing
         // between application contexts
        .setName("H2-" + COUNT.incrementAndGet())
        .setType(H2)
        .addScript("h2-schema.sql")
        .addScript("h2-data.sql")
        .build();
  }

}
