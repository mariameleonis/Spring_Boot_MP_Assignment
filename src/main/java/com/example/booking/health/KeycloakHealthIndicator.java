package com.example.booking.health;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class KeycloakHealthIndicator implements HealthIndicator {

  private final Keycloak keycloak;

  public KeycloakHealthIndicator(
      @Value("${keycloak.server.url}") final String keycloakServerUrl,
      @Value("${keycloak.realm}") final String keycloakRealm,
      @Value("${keycloak.client.id}") final String keycloakClientId,
      @Value("${keycloak.client.secret}") final String keycloakClientSecret,
      @Value("${keycloak.username}") final String keycloakUserName,
      @Value("${keycloak.password}") final String keycloakPassword
      ) {
    this.keycloak = KeycloakBuilder.builder()
        .serverUrl(keycloakServerUrl)
        .realm(keycloakRealm)
        .clientId(keycloakClientId)
        .clientSecret(keycloakClientSecret)
        .username(keycloakUserName)
        .password(keycloakPassword)
        .grantType(OAuth2Constants.PASSWORD)
        .build();
  }

  @Override
  public Health health() {
    try {
      keycloak.serverInfo().getInfo();
      return Health.up().build();
    } catch (Exception ex) {
      return Health.down()
          .withException(ex)
          .build();
    }
  }
}
