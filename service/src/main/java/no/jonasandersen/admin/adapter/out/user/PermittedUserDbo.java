package no.jonasandersen.admin.adapter.out.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;

@Entity
class PermittedUserDbo {

  @Id
  private Long id;

  private String subject;

  private String email;

  public Long id() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String subject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public @Email String email() {
    return email;
  }

  public void setEmail(@Email String email) {
    this.email = email;
  }
}
