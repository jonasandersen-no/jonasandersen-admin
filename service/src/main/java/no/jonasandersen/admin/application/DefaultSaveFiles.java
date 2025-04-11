package no.jonasandersen.admin.application;

import java.util.ArrayList;
import java.util.List;
import no.jonasandersen.admin.application.port.SaveFiles;
import no.jonasandersen.admin.domain.SaveFileOld;
import no.jonasandersen.admin.domain.SaveFileOld.SaveFileSnapshot;
import no.jonasandersen.admin.domain.User;
import org.springframework.stereotype.Service;

@Service
public class DefaultSaveFiles implements SaveFiles {

  private List<SaveFileSnapshot> files = new ArrayList<>();

  public DefaultSaveFiles() {}

  @Override
  public SaveFileOld create(String name) {
    SaveFileOld saveFileOld = new SaveFileOld(name);
    files.add(saveFileOld.snapshot());
    return saveFileOld;
  }

  @Override
  public List<SaveFileOld> findAll() {
    return files.stream().map(SaveFileOld::new).toList();
  }

  @Override
  public List<SaveFileOld> findAllBy(User owner) {
    return files.stream()
        .filter(snapshot -> snapshot.owner().equals(owner))
        .map(SaveFileOld::new)
        .toList();
  }

  @Override
  public void add(SaveFileOld saveFileOld) {
    files.add(saveFileOld.snapshot());
  }
}
