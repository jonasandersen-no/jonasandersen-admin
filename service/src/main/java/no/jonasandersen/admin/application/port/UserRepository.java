package no.jonasandersen.admin.application.port;

import java.util.HashMap;
import java.util.Map;
import no.jonasandersen.admin.domain.User;

public interface UserRepository {

  boolean existsByEmail(String email);

  User findByEmail(String email);

  void createNewUser(User user);

  Long getIdByEmail(String email);

  static UserRepository configureForTest() {
    return new InMemoryUserRepository();
  }

  class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public boolean existsByEmail(String email) {
      return users.containsKey(email);
    }

    @Override
    public User findByEmail(String email) {
      return users.get(email);
    }

    @Override
    public void createNewUser(User user) {
      users.put(user.username().value(), user);
    }

    @Override
    public Long getIdByEmail(String email) {
      return 0L;
    }
  }
}
