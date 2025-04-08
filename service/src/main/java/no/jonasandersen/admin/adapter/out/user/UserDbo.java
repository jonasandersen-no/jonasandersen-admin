package no.jonasandersen.admin.adapter.out.user;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import java.util.stream.Collectors;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.Username;

@Entity
@Table(name = "users")
class UserDbo {

  @Id @GeneratedValue Long id;

  @Column(unique = true, nullable = false)
  String username;

  @Convert(converter = RolesConverter.class)
  Set<RolesDbo> roles;

  @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "settings")
  private UserSettingsDbo settings;

  public UserDbo() {}

  public UserDbo(Username username) {
    this.username = username.value();
  }

  public UserDbo(Username username, Set<RolesDbo> roles) {
    this.username = username.value();
    this.roles = Set.copyOf(roles);
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
    return new User(
        Username.create(username),
        roles.stream().map(RolesDbo::toDomain).collect(Collectors.toUnmodifiableSet()));
  }
}
