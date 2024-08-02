package no.jonasandersen.admin.application;

import java.util.Set;
import java.util.function.UnaryOperator;
import no.jonasandersen.admin.application.port.UserRepository;
import no.jonasandersen.admin.domain.Roles;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {

  private static final Logger log = LoggerFactory.getLogger(DefaultUserService.class);
  private final UserRepository userRepository;

  public DefaultUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public static DefaultUserService configureForTest() {
    return configureForTest(UnaryOperator.identity());
  }

  public static DefaultUserService configureForTest(UnaryOperator<Config> configure) {
    Config config = configure.apply(new Config());
    return new DefaultUserService(config.userRepository);
  }

  @Override
  public User storeOrLoadUser(String email) {
    log.info("Storing or loading user with email {}", email);
    if (userRepository.existsByEmail(email)) {
      log.info("User with email {} already exists", email);
      return userRepository.findByEmail(email);
    }

    log.info("Creating new user with email {}", email);
    User user = new User(Username.create(email), Set.of(Roles.USER));
    userRepository.createNewUser(user);
    return user;
  }

  public static class Config {

    private final UserRepository userRepository = UserRepository.configureForTest();

    public Config addUsers(User... users) {
      for (User user : users) {
        userRepository.createNewUser(user);
      }
      return this;
    }
  }
}
