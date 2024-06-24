package no.jonasandersen.admin;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModuleTests {

  @Test
  void verify() {
    var modules = ApplicationModules.of(AdminApplication.class).verify();
  }
}
