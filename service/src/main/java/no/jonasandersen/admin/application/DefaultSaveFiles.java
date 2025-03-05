package no.jonasandersen.admin.application;

import java.util.ArrayList;
import java.util.List;
import no.jonasandersen.admin.application.port.SaveFiles;
import no.jonasandersen.admin.domain.SaveFile;
import no.jonasandersen.admin.domain.SaveFile.SaveFileSnapshot;
import no.jonasandersen.admin.domain.User;
import org.springframework.stereotype.Service;

@Service
public class DefaultSaveFiles implements SaveFiles {

  private List<SaveFileSnapshot> files = new ArrayList<>();

  public DefaultSaveFiles() {
  }

  @Override
  public SaveFile create(String name) {
    SaveFile saveFile = new SaveFile(name);
    files.add(saveFile.snapshot());
    return saveFile;
  }

  @Override
  public List<SaveFile> findAll() {
    return files.stream()
        .map(SaveFile::new)
        .toList();
  }

  @Override
  public List<SaveFile> findAllBy(User owner) {
    return files.stream()
        .filter(snapshot -> snapshot.owner().equals(owner))
        .map(SaveFile::new)
        .toList();
  }

  @Override
  public void add(SaveFile saveFile) {
    files.add(saveFile.snapshot());
  }
}
