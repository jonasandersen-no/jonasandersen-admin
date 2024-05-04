package fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;

public class TestShortcutRepository implements ShortcutRepository {

  private static final AtomicLong SEQUENCE = new AtomicLong(0);

  private final List<Shortcut> shortcuts = new ArrayList<>();

  @Override
  public Shortcut save(Shortcut shortcut) {
    Shortcut saved = Shortcut.assignId(shortcut, SEQUENCE.incrementAndGet());
    shortcuts.add(saved);
    return saved;
  }

  @Override
  public List<Shortcut> findAll() {
    return shortcuts;
  }

  /**
   * Find all shortcuts by project. Is case-insensitive.
   *
   * @param project The project to find shortcuts for
   * @return An unmodifiable list of shortcuts
   */
  @Override
  public List<Shortcut> findByProject(String project) {
    return shortcuts.stream()
        .filter(shortcut -> shortcut.project().equalsIgnoreCase(project))
        .toList();
  }

  @Override
  public void update(Shortcut shortcut) {
    shortcuts.removeIf(s -> s.id().equals(shortcut.id()));
    shortcuts.add(shortcut);
  }

  @Override
  public void delete(Long id) {
    shortcuts.removeIf(shortcut -> shortcut.id().equals(id));
  }
}
