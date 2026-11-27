package no.jonasandersen.admin.infrastructure;

import javax.sql.DataSource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.webauthn.management.JdbcPublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.JdbcUserCredentialRepository;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;

@Configuration
@Profile("!integration")
@EnableWebSecurity
class SecurityConfiguration {

  @Bean
  @Order(2)
  SecurityFilterChain securityFilterChainVue(HttpSecurity http, AdminProperties adminProperties) {
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    AdminProperties.WebAuthn webAuthn = adminProperties.webauthn();
    http.authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/", "/index.html", "/assets/**", "/favicon.ico").permitAll()
                    .requestMatchers("/api/public/**").permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest()
                    .permitAll())
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(requestHandler)
        )
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/api/login")
            .successHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()))
            .failureHandler((req, res, ex) -> res.setStatus(HttpStatus.UNAUTHORIZED.value()))
        ).logout(logout -> logout
            .logoutUrl("/api/logout")
            .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()))
        ).webAuthn(webauthn -> webauthn
            .rpId(webAuthn.rpId())
            .rpName(webAuthn.rpName())
            .allowedOrigins(webAuthn.allowedOrigins().toArray(new String[0]))
        );
    http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

  @Bean
  UserCredentialRepository userCredentialRepository(JdbcOperations jdbcOperations) {
    return new JdbcUserCredentialRepository(jdbcOperations);
  }

  @Bean
  PublicKeyCredentialUserEntityRepository publicKeyCredentialUserEntityRepository(
      JdbcOperations jdbcOperations) {
    return new JdbcPublicKeyCredentialUserEntityRepository(jdbcOperations);
  }

  @Bean
  AuthenticationEventPublisher authenticationEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) {
    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
  }
}
