package no.jonasandersen.admin.application;

import java.util.List;
import no.jonasandersen.admin.application.port.AccessControlRepository;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.UserAlreadyExistsException;
import no.jonasandersen.admin.domain.UserNotFoundException;

public class DefaultAccessControl implements AccessControl {

  private final AccessControlRepository repository;

  public DefaultAccessControl(AccessControlRepository repository) {
    this.repository = repository;
  }

  @Override
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

  @Override
  public List<User> getAllowedUsers() {
    return List.copyOf(repository.findAll());
  }

  @Override
  public boolean isUserAllowed(String email) {
    return doesUserExist(email);
  }

  @Override
  public void revokeUser(String email) {
    User user = repository.findUser(email);

    if (user == null) {
      throw new UserNotFoundException();
    }

    repository.removeUser(user);
  }
}
