package com.example.booking.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

  @Bean
  @ConfigurationProperties("datasource.primary")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("datasource.primary.hikari")
  public DataSource dataSource() {
    return dataSourceProperties().initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

}
