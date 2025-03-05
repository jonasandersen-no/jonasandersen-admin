package no.jonasandersen.admin.application.port;

import java.util.List;
import no.jonasandersen.admin.domain.SaveFile;
import no.jonasandersen.admin.domain.User;

public interface SaveFiles {

  SaveFile create(String name);

  List<SaveFile> findAll();

  List<SaveFile> findAllBy(User owner);

  void add(SaveFile saveFile);
}
