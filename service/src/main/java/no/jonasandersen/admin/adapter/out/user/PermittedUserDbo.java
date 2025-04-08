package no.jonasandersen.admin.adapter.out.user;

import no.jonasandersen.admin.domain.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "permitted_users")
public final class PermittedUserDbo {

  @Id private Long id;
  private String email;

  public PermittedUserDbo() {}

  public PermittedUserDbo(Long id, String email) {
    this.id = id;
    this.email = email;
  }

  public User toUser() {
    return User.createUser(this.email);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "PermittedUserDbo[" + "id=" + id + ", " + "email=" + email + ']';
  }
}
