package no.jonasandersen.admin.adapter.out.linode;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import org.junit.jupiter.api.Test;

class LinodeServerApiTest {

  @Test
  void callingListServerInfoReturnsServerInfo() {
    ServerApi api = new LinodeServerApi(new TestLinodeBjoggisExchange(), new TestLinodeExchange());
    MinecraftInstance response = api.listServerInfo();

    assertThat(response.getName())
        .isEqualTo("Server name");
    assertThat(response.getIp().value())
        .isEqualTo("127.0.0.1");
    assertThat(response.getStatus())
        .isEqualTo("Running");
  }
}