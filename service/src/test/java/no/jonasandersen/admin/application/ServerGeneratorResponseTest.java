package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.ServerGeneratorResponse;
import org.junit.jupiter.api.Test;

class ServerGeneratorResponseTest {

  @Test
  void labelAndIpExistsWhenFromLinodeInstanceIsCalled() {
    ServerGeneratorResponse response = ServerGeneratorResponse.from(
        LinodeInstance.createNull("label", List.of("127.0.0.1")));

    assertThat(response.label()).isEqualTo("label");
    assertThat(response.ip()).isEqualTo(new Ip("127.0.0.1"));
  }
}