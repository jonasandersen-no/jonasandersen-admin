package no.jonasandersen.admin.infrastructure;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@Profile("!integration")
@EnableWebSecurity
class SecurityConfiguration {

  @Bean
  @Order(1)
  SecurityFilterChain securityFilterChainResourceServer(HttpSecurity http) {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
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
  @Order(2)
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      UserDetailsService userDetailsService,
      PersistentTokenRepository persistentTokenRepository) {

    http.authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/actuator/**")
                    .hasAuthority("ACTUATOR")
                    .anyRequest()
                    .authenticated())
        .formLogin(withDefaults())
        .rememberMe(
            remember ->
                remember
                    .tokenRepository(persistentTokenRepository)
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds(86400 * 7))
        .userDetailsService(userDetailsService);
    return http.build();
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    return tokenRepository;
  }

  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

  @Bean
  AuthenticationEventPublisher authenticationEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) {
    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
  }
}
