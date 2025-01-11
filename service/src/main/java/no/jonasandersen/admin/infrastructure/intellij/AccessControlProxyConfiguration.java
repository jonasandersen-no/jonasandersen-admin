package no.jonasandersen.admin.infrastructure.intellij;

import java.util.List;
import no.jonasandersen.admin.application.AccessControl;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.UserAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("intellij")
class AccessControlProxyConfiguration {

  private static final Logger log = LoggerFactory.getLogger(AccessControlProxyConfiguration.class);

  private AccessControlProxyConfiguration() {
  }

  @Bean
  static BeanPostProcessor accessControlProxyBeanPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName)
          throws BeansException {
        record ProxiedAccessControl(AccessControl delegate) implements AccessControl {

          @Override
          public User allowUser(String email) {
            return delegate.allowUser(email);
          }

          @Override
          public List<User> getAllowedUsers() {
            return delegate.getAllowedUsers();
          }

          @Override
          public boolean isUserAllowed(String email) {
            try {
              allowUser(email);
            } catch (UserAlreadyExistsException _) {

            }

            return delegate.isUserAllowed(email);
          }

          @Override
          public void revokeUser(String email) {
            delegate.revokeUser(email);
          }
        }

        if (bean instanceof AccessControl accessControl) {
          try {
            return new ProxiedAccessControl(accessControl);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
      }
    };
  }

}
