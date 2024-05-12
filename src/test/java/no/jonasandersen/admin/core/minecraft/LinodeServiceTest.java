package no.jonasandersen.admin.core.minecraft;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.domain.SensitiveString;
import org.junit.jupiter.api.Test;

class LinodeServiceTest {

  @Test
  void principalNameIsAddedAsTagWhenCreatingInstance() {
    LinodeService service = LinodeService.createNull();

    LinodeInstance instance = service.createDefaultMinecraftInstance(SensitiveString.of("Password123!"));
    assertThat(instance.tags()).contains("principalName");
  }
}