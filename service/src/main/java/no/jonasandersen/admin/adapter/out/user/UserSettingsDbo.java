package no.jonasandersen.admin.adapter.out.user;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "user_settings")
class UserSettingsDbo {

  @Id
  private Long id;

  private String theme;

  public UserSettingsDbo() {
  }

  public UserSettingsDbo(UserDbo userDbo, String value) {
    this.theme = value;

    userDbo.setSettings(this);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
           ", theme='" + theme + '\'' +
           '}';
  }
}
