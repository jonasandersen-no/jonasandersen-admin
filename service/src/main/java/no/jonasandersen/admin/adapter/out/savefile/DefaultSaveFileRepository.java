package no.jonasandersen.admin.adapter.out.savefile;

import no.jonasandersen.admin.application.port.SaveFileRepository;
import no.jonasandersen.admin.application.port.UserRepository;
import no.jonasandersen.admin.domain.SaveFile;
import org.springframework.stereotype.Service;

@Service
class DefaultSaveFileRepository implements SaveFileRepository {

  private final JdbcSaveFileRepository repository;
  private final UserRepository userRepository;

  DefaultSaveFileRepository(JdbcSaveFileRepository repository, UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  @Override
  public void save(SaveFile saveFile) {
    SaveFileDbo dbo = new SaveFileDbo();
    Long user = userRepository.getIdByEmail(saveFile.getOwner().username().value());
    dbo.setOwner(user);
    dbo.setName(saveFile.getName());

    dbo.validate();
    repository.save(dbo);
  }
}
