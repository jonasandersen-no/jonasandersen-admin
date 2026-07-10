package no.jonasandersen.admin.infrastructure;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;
import no.jonasandersen.admin.user.PermittedUserFilter;
import no.jonasandersen.admin.user.AccessControl;
import no.jonasandersen.admin.user.DefaultOidcUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Profile("!integration")
@EnableWebSecurity
class SecurityConfiguration {

  @Bean
  @Order(1)
  SecurityFilterChain securityFilterChainResourceServer(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(_ -> withDefaults())
        .securityMatcher("/api/**")
        .authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/api/temperature")
                    .authenticated()
                    .requestMatchers("/api/commands/**")
                    .permitAll()
                    .requestMatchers("/api/**")
                    .authenticated())
        .oauth2ResourceServer(configurer -> configurer.jwt(withDefaults()))
        .httpBasic(withDefaults());

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
    configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "DELETE"));
    configuration.setAllowedHeaders(List.of("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
  @Bean
  @Order(2)
  SecurityFilterChain securityFilterChain(
      HttpSecurity http, AccessControl accessControl, DefaultOidcUserService defaultOidcUserService) {

    http.authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/actuator/**")
                    .hasRole("ACTUATOR")
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(new PermittedUserFilter(accessControl), AuthorizationFilter.class)
        .oauth2Login(
            c -> c.userInfoEndpoint(userInfo -> userInfo.oidcUserService(defaultOidcUserService)))
        .oauth2Client(withDefaults())
        .httpBasic(withDefaults());
    return http.build();
  }

  @Bean
  AuthenticationEventPublisher authenticationEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) {
    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
  }
}
