package no.jonasandersen.admin.infrastructure;

import static org.springframework.security.config.Customizer.withDefaults;

import no.jonasandersen.admin.adapter.out.user.PermittedUsers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

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
  SecurityFilterChain securityFilterChain(HttpSecurity http, PermittedUsers permittedUsers) throws Exception {
    http
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests.anyRequest().authenticated()
        )
        .addFilterBefore(new PermittedUserFilter(permittedUsers), AuthorizationFilter.class)
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
