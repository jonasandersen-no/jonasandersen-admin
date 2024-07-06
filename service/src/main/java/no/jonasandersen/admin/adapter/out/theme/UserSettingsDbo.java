package no.jonasandersen.admin.adapter.out.theme;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_settings", schema = "admin")
public class UserSettingsDbo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String theme;

  public UserSettingsDbo() {
  }

  public UserSettingsDbo(String username, String theme) {
    this.username = username;
    this.theme = theme;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  @Override
  public String toString() {
    return "UserSettingsDbo{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", theme='" + theme + '\'' +
        '}';
  }
}
