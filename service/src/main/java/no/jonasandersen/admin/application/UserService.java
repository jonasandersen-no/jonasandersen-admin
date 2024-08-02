package no.jonasandersen.admin.application;

import no.jonasandersen.admin.domain.User;

public interface UserService {

  User storeOrLoadUser(String email);
}
