package no.jonasandersen.admin.adapter.out.websocket;

import java.util.HashMap;
import java.util.Map;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;

public record Model(Map<String, Object> model) {

  public static Model from(Shortcut shortcut) {
    Map<String, Object> model = new HashMap<>();
    model.put("shortcut", shortcut);
    return new Model(model);
  }

  public static Model from(LinodeInstance linodeInstance) {
    Map<String, Object> model = new HashMap<>();
    model.put("instance", linodeInstance);
    return new Model(model);
  }
}
