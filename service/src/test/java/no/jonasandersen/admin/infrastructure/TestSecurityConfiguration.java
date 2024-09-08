package no.jonasandersen.admin.infrastructure;

import static org.springframework.security.config.Customizer.withDefaults;

import no.jonasandersen.admin.application.AccessControl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@TestConfiguration
public class TestSecurityConfiguration {

  @Bean
  @Order(1)
  SecurityFilterChain securityFilterChainResourceServer(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .securityMatcher("/api/**")
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers("/api/temperature").permitAll()
                .requestMatchers("/api/**").authenticated())
        .oauth2ResourceServer(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  @Order(2)
  SecurityFilterChain securityFilterChain(HttpSecurity http, AccessControl accessControl) throws Exception {
    http
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/control-center").hasRole("ADMIN")
            .requestMatchers("/actuator/**").hasRole("ACTUATOR")
            .anyRequest().authenticated())
        .addFilterBefore(new PermittedUserFilter(accessControl), AuthorizationFilter.class)
        .oauth2Login(AbstractHttpConfigurer::disable)
        .oauth2Client(AbstractHttpConfigurer::disable)
        .httpBasic(withDefaults());
    return http.build();
  }
}