package no.jonasandersen.admin.application.port;

import java.util.List;
import no.jonasandersen.admin.domain.User;

public interface AccessControlRepository {

  void addUser(User user);

  User findUser(String email);

  List<User> findAll();
}
