package no.jonasandersen.admin.core.shortcut;

import java.util.List;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;

public class ShortcutService {

  private final ShortcutRepository repository;

  public ShortcutService(ShortcutRepository repository) {
    this.repository = repository;
  }

  public Shortcut createShortcut(String project, String keymap, String description) {
    Shortcut shortcut = new Shortcut(null, project, keymap, description);
    return repository.save(shortcut);
  }

  public List<Shortcut> getShortcutsByProject(String project) {
    return repository.findByProject(project);
  }

  public List<Shortcut> getShortcuts() {
    return repository.findAll();
  }
}
