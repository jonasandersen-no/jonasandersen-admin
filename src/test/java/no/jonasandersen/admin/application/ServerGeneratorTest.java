package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.core.minecraft.domain.Ip;
import org.junit.jupiter.api.Test;

class ServerGeneratorTest {

  @Test
  void createInstanceWhenCalled() {
    ServerGenerator generator = ServerGenerator.createNull();

    var response = generator.generate();

    assertThat(response).isNotNull();
    assertThat(response.ip()).isEqualTo(new Ip("127.0.0.1"));
    assertThat(response.label()).isEqualTo("label");
  }
}