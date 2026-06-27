package no.jonasandersen.admin.user;

import java.util.List;

public interface AccessControl {

  User allowUser(String email);

  List<User> getAllowedUsers();

  boolean isUserAllowed(String email);

  void revokeUser(String email);
}
