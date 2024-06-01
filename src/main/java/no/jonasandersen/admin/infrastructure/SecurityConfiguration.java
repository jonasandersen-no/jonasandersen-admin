package no.jonasandersen.admin.infrastructure;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfiguration {

  @Bean
  @Order(1)
  SecurityFilterChain securityFilterChainResourceServer(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .securityMatcher("/api/**")
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests.requestMatchers("/api/**").authenticated()
        )
        .oauth2ResourceServer(configurer -> configurer.jwt(withDefaults()));
    return http.build();
  }

  @Bean
  @Order(2)
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests.anyRequest().authenticated()
        )
        .oauth2Login(withDefaults())
        .oauth2Client(withDefaults());
    return http.build();
  }

  @Bean
  AuthenticationEventPublisher authenticationEventPublisher
      (ApplicationEventPublisher applicationEventPublisher) {
    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
  }
}
