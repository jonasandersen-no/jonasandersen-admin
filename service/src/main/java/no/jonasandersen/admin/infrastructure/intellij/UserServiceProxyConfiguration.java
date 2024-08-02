package no.jonasandersen.admin.infrastructure.intellij;

import java.util.Set;
import no.jonasandersen.admin.application.UserService;
import no.jonasandersen.admin.domain.Roles;
import no.jonasandersen.admin.domain.User;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/*
  This configuration is only active when the "intellij" profile is active. It creates a proxy for
  the UserService bean that adds all roles to the user.
 */
@Configuration
@Profile("intellij")
public class UserServiceProxyConfiguration {

  private static final Logger log = LoggerFactory.getLogger(UserServiceProxyConfiguration.class);

  @Bean
  BeanPostProcessor userServiceProxyBeanPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName)
          throws BeansException {
        record ProxiedUserService(UserService delegate) implements UserService {

          @Override
          public User storeOrLoadUser(String email) {
            User user = delegate.storeOrLoadUser(email);
            log.info("Injecting all roles for user {}", user.username());
            return new User(user.username(), Set.of(Roles.values()));
          }
        }

        if (bean instanceof UserService userService) {
          try {
            return new ProxiedUserService(userService);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
      }
    };
  }
}
