package no.jonasandersen.admin.adapter.out.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import no.jonasandersen.admin.domain.User;

@Entity
@Table(name = "permitted_users")
public class PermittedUserDbo {

  @Id
  @GeneratedValue
  private Long id;

  private String email;

  public Long id() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public @Email String email() {
    return email;
  }

  public void setEmail(@Email String email) {
    this.email = email;
  }

  public User toUser() {
    return User.createUser(this.email);
  }
}
