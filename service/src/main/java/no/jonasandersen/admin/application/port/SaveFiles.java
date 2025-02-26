package no.jonasandersen.admin.application.port;

import java.util.List;
import no.jonasandersen.admin.domain.SaveFile;

public interface SaveFiles {

  SaveFile create(String name);

  List<SaveFile> findAll();
}
