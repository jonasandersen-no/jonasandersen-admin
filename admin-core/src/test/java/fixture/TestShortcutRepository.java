package fixture;

import java.util.ArrayList;
import java.util.List;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;

public class TestShortcutRepository implements ShortcutRepository {

  private final List<Shortcut> shortcuts = new ArrayList<>();

  @Override
  public Shortcut save(Shortcut shortcut) {
    shortcuts.add(shortcut);
    return shortcut;
  }

  @Override
  public List<Shortcut> findAll() {
    return shortcuts;
  }
}
