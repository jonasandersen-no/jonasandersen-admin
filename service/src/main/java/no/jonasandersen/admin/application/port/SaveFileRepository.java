package no.jonasandersen.admin.application.port;

import no.jonasandersen.admin.domain.SaveFile;

public interface SaveFileRepository {

  void save(SaveFile saveFile);
}
