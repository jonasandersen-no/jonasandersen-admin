package no.jonasandersen.admin.adapter.out.theme;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "user_settings", schema = "admin")
class UserSettingsDbo {

  @Id
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
