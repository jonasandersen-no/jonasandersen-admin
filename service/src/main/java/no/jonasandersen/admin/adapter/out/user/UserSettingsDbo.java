package no.jonasandersen.admin.adapter.out.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_settings")
class UserSettingsDbo {

  @Id
  @GeneratedValue
  private Long id;

  @OneToOne(mappedBy = "settings")
  private UserDbo user;

  private String theme;

  public UserSettingsDbo() {
  }

  public UserSettingsDbo(UserDbo userDbo, String value) {
    this.user = userDbo;
    this.theme = value;

    userDbo.setSettings(this);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserDbo getUser() {
    return user;
  }

  public void setUser(UserDbo user) {
    this.user = user;
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
        ", user=" + user +
        ", theme='" + theme + '\'' +
        '}';
  }
}
