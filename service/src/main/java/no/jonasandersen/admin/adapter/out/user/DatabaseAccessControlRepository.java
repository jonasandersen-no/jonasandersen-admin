package no.jonasandersen.admin.adapter.out.user;

import java.util.List;
import no.jonasandersen.admin.application.port.AccessControlRepository;
import no.jonasandersen.admin.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class DatabaseAccessControlRepository implements AccessControlRepository {

  private final CrudPermittedUserRepository repository;

  DatabaseAccessControlRepository(CrudPermittedUserRepository repository) {
    this.repository = repository;
  }

  @Override
  public void addUser(User user) {
    PermittedUserDbo entity = new PermittedUserDbo();
    entity.setEmail(user.username().value());
    repository.save(entity);
  }

  @Override
  public User findUser(String email) {
    PermittedUserDbo entity = repository.findByEmail(email);
    if (entity == null) {
      return null;
    }

    return repository.findByEmail(email)
        .toUser();
  }

  @Override
  public List<User> findAll() {
    return repository.findAll()
        .stream()
        .map(PermittedUserDbo::toUser)
        .toList();
  }

  @Override
  public void removeUser(User user) {
    repository.deleteByEmail(user.username().value());
  }
}
