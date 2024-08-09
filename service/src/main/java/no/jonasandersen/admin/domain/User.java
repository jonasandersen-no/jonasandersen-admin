package no.jonasandersen.admin.domain;

import java.util.Objects;
import java.util.Set;

public final class User {

  private Username username;
  private Set<Roles> roles;

  public User() {
  }

  public User(Username username, Set<Roles> roles) {
    this.username = username;
    this.roles = roles;
  }

  public static User createUser(String email) {
    User user = new User();
    user.setUsername(Username.of(email));
    user.setRoles(Set.of(Roles.USER));
    return user;
  }

  public Username username() {
    return username;
  }

  public void setUsername(Username username) {
    this.username = username;
  }

  public Set<Roles> roles() {
    return roles;
  }

  public void setRoles(Set<Roles> roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (User) obj;
    return Objects.equals(this.username, that.username) &&
        Objects.equals(this.roles, that.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, roles);
  }

  @Override
  public String toString() {
    return "User[" +
        "username=" + username + ", " +
        "roles=" + roles + ']';
  }


}
