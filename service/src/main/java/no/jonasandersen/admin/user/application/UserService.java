package no.jonasandersen.admin.user.application;

import no.jonasandersen.admin.user.User;

public interface UserService {

  User storeOrLoadUser(String email);
}
