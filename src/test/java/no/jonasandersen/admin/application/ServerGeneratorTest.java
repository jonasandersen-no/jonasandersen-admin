package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ServerGeneratorTest {

  @Test
  void createInstanceWhenCalled() {
    ServerGenerator generator = ServerGenerator.createNull();

    Object response = generator.generate();


    assertThat(response).isNotNull();
  }
}