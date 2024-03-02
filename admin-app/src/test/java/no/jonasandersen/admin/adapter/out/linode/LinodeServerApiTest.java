package no.jonasandersen.admin.adapter.out.linode;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import org.junit.jupiter.api.Test;

class LinodeServerApiTest {

  @Test
  void callingListServerInfoReturnsServerInfo() {
    ServerApi api = new LinodeServerApi(new TestLinodeExchange());
    String response = api.listServerInfo();

    assertThat(response)
        .isEqualTo("Server info");
  }
}