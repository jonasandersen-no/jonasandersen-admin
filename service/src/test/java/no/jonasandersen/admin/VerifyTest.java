package no.jonasandersen.admin;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class VerifyTest {

  @Test
  void verify() {
    ApplicationModules modules = ApplicationModules.of(AdminApplication.class).verify();

    new Documenter(modules).writeModulesAsPlantUml().writeIndividualModulesAsPlantUml();
  }
}
