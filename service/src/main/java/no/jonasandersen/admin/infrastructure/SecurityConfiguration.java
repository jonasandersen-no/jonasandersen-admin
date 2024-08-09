package no.jonasandersen.admin.infrastructure;

import static org.springframework.security.config.Customizer.withDefaults;

import no.jonasandersen.admin.application.AccessControl;
import no.jonasandersen.admin.infrastructure.security.DefaultOidcUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@Profile("!integration")
@EnableMethodSecurity
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
  SecurityFilterChain securityFilterChain(HttpSecurity http, AccessControl accessControl,
      DefaultOidcUserService defaultOidcUserService) throws Exception {
    http
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/control-center").hasRole("ADMIN")
            .requestMatchers("/actuator/**").hasRole("ACTUATOR")
            .anyRequest().authenticated()
        )
        .addFilterBefore(new PermittedUserFilter(accessControl), AuthorizationFilter.class)
        .oauth2Login(c ->
            c.userInfoEndpoint(userInfo ->
                userInfo.oidcUserService(defaultOidcUserService)))
        .oauth2Client(withDefaults())
        .httpBasic(withDefaults());
    return http.build();
  }


  @Bean
  AuthenticationEventPublisher authenticationEventPublisher
      (ApplicationEventPublisher applicationEventPublisher) {
    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
  }

}
