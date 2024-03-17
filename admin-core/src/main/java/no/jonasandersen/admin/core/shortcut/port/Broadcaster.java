package no.jonasandersen.admin.core.shortcut.port;

import no.jonasandersen.admin.core.shortcut.domain.Shortcut;

public interface Broadcaster {

  void onShortcutCreated(Shortcut shortcut);

  void onShortcutUpdated(Shortcut shortcut);
}
