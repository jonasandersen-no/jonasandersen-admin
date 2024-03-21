package no.jonasandersen.admin.core.shortcut;

import java.util.List;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import no.jonasandersen.admin.core.shortcut.port.Broadcaster;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;

public class ShortcutService {

  private final ShortcutRepository repository;
  private final List<Broadcaster> broadcasters;

  public ShortcutService(ShortcutRepository repository,
      List<Broadcaster> broadcasters) {
    this.repository = repository;
    this.broadcasters = broadcasters;
  }

  public Shortcut createShortcut(String project, String keymap, String description) {
    Shortcut shortcut = new Shortcut(null, project, keymap, description);
    repository.save(shortcut);

    List<Shortcut> byProject = repository.findByProject(project);
    Shortcut saved = byProject.stream()
        .filter(s -> keymap.equalsIgnoreCase(s.shortcut()) &&
            description.equalsIgnoreCase(s.description()))
        .findFirst().orElseThrow();

    broadcasters.forEach(broadcaster -> broadcaster.onShortcutCreated(saved));
    return saved;
  }

  public List<Shortcut> getShortcutsByProject(String project) {
    return repository.findByProject(project);
  }

  public List<Shortcut> getShortcuts() {
    return repository.findAll();
  }

  public void updateShortcut(Shortcut shortcut) {
    repository.update(shortcut);
    broadcasters.forEach(broadcaster -> broadcaster.onShortcutUpdated(shortcut));
  }

  public void deleteShortcut(Long id) {
    repository.delete(id);
    broadcasters.forEach(broadcaster -> broadcaster.onShortcutDeleted(id));
  }
}
