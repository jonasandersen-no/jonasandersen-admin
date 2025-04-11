package no.jonasandersen.admin.application.port;

import no.jonasandersen.admin.domain.SaveFileOld;

public interface SaveFileRepository {

  void save(SaveFileOld saveFileOld);
}
