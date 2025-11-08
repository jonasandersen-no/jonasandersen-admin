package no.jonasandersen.admin.adapter.out.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Sequence;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "user_settings")
class UserSettingsDbo {

  @Id @Sequence("user_settings_seq")
  private Long id;

  @Column private String theme;

  public UserSettingsDbo() {}

  public UserSettingsDbo(String value) {
    this.theme = value;
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
    return "UserSettingsDbo{" + "id=" + id + ", theme='" + theme + '\'' + '}';
  }
}
