package com.example.booking.health;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

  private final DataSource dataSource;

  @Override
  public Health health() {
    try (Connection connection = dataSource.getConnection()) {
      if (connection.isValid(1)) {
        return Health.up().build();
      } else {
        return Health.down()
            .withDetail("reason", "Database connection is not valid")
            .build();
      }
    } catch (SQLException ex) {
      return Health.down()
          .withException(ex)
          .build();
    }
  }
}