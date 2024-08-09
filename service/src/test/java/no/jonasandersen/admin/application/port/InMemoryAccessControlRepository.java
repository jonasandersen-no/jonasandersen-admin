package no.jonasandersen.admin.application.port;

import java.util.ArrayList;
import java.util.List;
import no.jonasandersen.admin.domain.User;

public class InMemoryAccessControlRepository implements AccessControlRepository {

  private final List<User> users = new ArrayList<>();

  @Override
  public void addUser(User user) {
    users.add(user);
  }

  @Override
  public User findUser(String email) {
    return users.stream()
        .filter(user -> user.username().value().equals(email))
        .findFirst().orElse(null);
  }

  @Override
  public List<User> findAll() {
    return List.copyOf(users);
  }

  @Override
  public void removeUser(User user) {
    users.remove(user);
  }

}
