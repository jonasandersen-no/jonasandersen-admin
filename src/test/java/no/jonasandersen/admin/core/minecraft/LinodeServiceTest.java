package no.jonasandersen.admin.core.minecraft;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.core.domain.LinodeInstance;
import org.junit.jupiter.api.Test;

class LinodeServiceTest {

  @Test
  void principalNameIsAddedAsTagWhenCreatingInstance() {
    LinodeService service = LinodeService.createNull();

    LinodeInstance instance = service.createDefaultMinecraftInstance();

    assertThat(instance.tags()).contains("principalName");
  }
}