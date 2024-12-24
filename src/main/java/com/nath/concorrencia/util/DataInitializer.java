package com.nath.concorrencia.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import java.nio.charset.StandardCharsets;

import static java.text.MessageFormat.format;

@Component
public class DataInitializer {

  @Autowired
  private JdbcTemplate jdbcTemplate;
  private final String[] tables = {"clients", "categories", "products"};

  @Bean
  public ApplicationRunner initializeData() {
    return args -> {
      for(String table : tables) {
        String scriptName = format("/db/{0}.sql", table);
        String sqlScript = StreamUtils.copyToString(getClass().getResourceAsStream(scriptName), StandardCharsets.UTF_8);
        jdbcTemplate.execute(sqlScript);
      }
    };
  }
}
