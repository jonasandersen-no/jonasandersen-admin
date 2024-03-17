package no.jonasandersen.admin.core.shortcut.port;

import java.util.List;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;

public interface ShortcutRepository {

  Shortcut save(Shortcut shortcut);

  List<Shortcut> findAll();

  List<Shortcut> findByProject(String project);

  void update(Shortcut shortcut);
}
