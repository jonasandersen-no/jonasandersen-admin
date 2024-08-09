package no.jonasandersen.admin.application;

import java.util.List;
import no.jonasandersen.admin.application.port.AccessControlRepository;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.UserAlreadyExistsException;

public class AccessControl {

  private final AccessControlRepository repository;

  public AccessControl(AccessControlRepository repository) {
    this.repository = repository;
  }

  public User allowUser(String email) {
    if (doesUserExist(email)) {
      throw new UserAlreadyExistsException();
    }

    User user = User.createUser(email);
    repository.addUser(user);
    return user;
  }

  private boolean doesUserExist(String email) {
    User user = repository.findUser(email);
    return user != null;
  }

  public List<User> getAllowedUsers() {
    return List.copyOf(repository.findAll());
  }

  public boolean isUserAllowed(String email) {
    return doesUserExist(email);
  }
}
