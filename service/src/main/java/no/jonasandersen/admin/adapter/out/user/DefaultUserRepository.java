package no.jonasandersen.admin.adapter.out.user;

import java.util.Set;
import java.util.stream.Collectors;
import no.jonasandersen.admin.application.port.UserRepository;
import no.jonasandersen.admin.domain.User;
import org.springframework.stereotype.Service;

@Service
class DefaultUserRepository implements UserRepository {

  private final CrudUserDboRepository repository;

  public DefaultUserRepository(CrudUserDboRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean existsByEmail(String email) {
    return repository.existsByUsername(email);
  }

  @Override
  public User findByEmail(String email) {
    UserDbo userDbo = repository.findByUsername(email).orElseThrow();
    return userDbo.toDomain();
  }

  @Override
  public void createNewUser(User user) {
    UserDbo userDbo = new UserDbo(user.username());

    Set<RolesDbo> roles = user.roles().stream()
        .map(RolesDbo::fromDomain)
        .collect(Collectors.toSet());
    userDbo.setRoles(roles);

    repository.save(userDbo);
  }
}
