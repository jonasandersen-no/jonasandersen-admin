package no.jonasandersen.admin.application;

import java.util.List;
import no.jonasandersen.admin.domain.User;

public interface AccessControl {

  User allowUser(String email);

  List<User> getAllowedUsers();

  boolean isUserAllowed(String email);

  void revokeUser(String email);
}
