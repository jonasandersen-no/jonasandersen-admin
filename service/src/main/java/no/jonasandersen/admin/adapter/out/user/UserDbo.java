package no.jonasandersen.admin.adapter.out.user;

import java.util.Set;
import java.util.stream.Collectors;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.Username;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
class UserDbo {

  @Id
  Long id;

  String username;

  @Transient
  Set<RolesDbo> roles;

  @MappedCollection(idColumn = "id")
  private UserSettingsDbo settings;

  public UserDbo() {
  }

  public UserDbo(Username username) {
    this.username = username.value();
  }

  public UserDbo(Username username, Set<RolesDbo> roles) {
    this.username = username.value();
    this.roles = Set.copyOf(roles);
  }

  @Column("roles")
  public String getRolesAsString() {
    RolesSetToStringConverter converter = new RolesSetToStringConverter();
    return converter.convert(roles);
  }

  public void setRolesAsString(String roles) {
    StringToRolesSetConverter converter = new StringToRolesSetConverter();
    this.roles = converter.convert(roles);
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

  public Set<RolesDbo> getRoles() {
    return roles;
  }

  public void setRoles(Set<RolesDbo> roles) {
    this.roles = roles;
  }

  public UserSettingsDbo getSettings() {
    return settings;
  }

  public void setSettings(UserSettingsDbo settings) {
    this.settings = settings;
  }

  public User toDomain() {
    return new User(Username.create(username),
        roles.stream().map(RolesDbo::toDomain).collect(Collectors.toUnmodifiableSet()));
  }
}
