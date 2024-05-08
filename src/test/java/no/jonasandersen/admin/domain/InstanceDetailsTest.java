package no.jonasandersen.admin.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InstanceDetailsTest {

  @Test
  void defaultMinecraftSetupIsAsExpected() {
    InstanceDetails details = InstanceDetails.createDefaultMinecraft("password");

    assertThat(details.region()).isEqualTo("se-sto");
    assertThat(details.image()).isEqualTo("linode/ubuntu22.04");
    assertThat(details.label()).startsWith("minecraft-auto-config-");
    assertThat(details.type()).isEqualTo("g6-dedicated-4");
    assertThat(details.tags()).containsExactly("minecraft", "auto-created");
    assertThat(details.rootPassword()).isNotBlank();
  }
}