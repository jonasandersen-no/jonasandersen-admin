package no.jonasandersen.admin.adapter.out.linode;

import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.SensitiveString;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class CreateInstanceRequestTest {

  @Test
  void convertFromInstanceDetails() {
    InstanceDetails instanceDetails = InstanceDetails.createDefaultMinecraft(SensitiveString.of("password"));

    CreateInstanceRequest request = CreateInstanceRequest.from(instanceDetails);

    assertThat(request.region()).isEqualTo("se-sto");
    assertThat(request.image()).isEqualTo("private/30572286");
    assertThat(request.label()).startsWith("minecraft-auto-config-");
    assertThat(request.type()).isEqualTo("g6-dedicated-4");
    assertThat(request.tags()).containsExactly("minecraft", "auto-created");
    assertThat(request.rootPassword()).isEqualTo("password");
    assertThat(request.volume()).isTrue();
  }
}