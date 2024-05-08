package no.jonasandersen.admin.adapter.out.linode;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.domain.InstanceDetails;
import org.junit.jupiter.api.Test;

class LinodeServerApiTest {

  @Test
  void instanceCreatedWhenDetailsIsProvided() {
    LinodeServerApi serverApi = LinodeServerApi.createNull();

    LinodeInstance instance = serverApi.createInstance(InstanceDetails.createDefaultMinecraft("password"));

    assertThat(instance).isNotNull();
    assertThat(instance.linodeId()).isEqualTo(LinodeId.from(1L));
    assertThat(instance.label()).startsWith("minecraft-auto-config-");

  }
}