package no.jonasandersen.admin.user.application.port;

import java.util.List;
import no.jonasandersen.admin.user.User;

public interface AccessControlRepository {

  void addUser(User user);

  User findUser(String email);

  List<User> findAll();

  void removeUser(User user);
}
