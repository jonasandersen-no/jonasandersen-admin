package no.jonasandersen.admin.application.port;

import java.util.List;
import no.jonasandersen.admin.domain.SaveFileOld;
import no.jonasandersen.admin.domain.User;

public interface SaveFiles {

  SaveFileOld create(String name);

  List<SaveFileOld> findAll();

  List<SaveFileOld> findAllBy(User owner);

  void add(SaveFileOld saveFileOld);
}
