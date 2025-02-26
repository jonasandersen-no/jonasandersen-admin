package no.jonasandersen.admin.application;

import java.util.ArrayList;
import java.util.List;
import no.jonasandersen.admin.application.port.SaveFiles;
import no.jonasandersen.admin.domain.SaveFile;

public class DefaultSaveFiles implements SaveFiles {

  private List<SaveFile> files = new ArrayList<>();

  @Override
  public SaveFile create(String name) {
    SaveFile saveFile = new SaveFile(name);
    files.add(saveFile);
    return saveFile;
  }

  @Override
  public List<SaveFile> findAll() {
    return List.copyOf(files);
  }
}
