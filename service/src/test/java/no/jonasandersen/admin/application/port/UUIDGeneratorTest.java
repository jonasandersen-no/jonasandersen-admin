package no.jonasandersen.admin.application.port;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UUIDGeneratorTest {

  @Test
  void generateUUIDAndCheckTimestamp() {

    UUID uuid = UUIDGenerator.generate();

    Instant time = UUIDGenerator.getTime(uuid);

    assertThat(time).isNotNull();
  }
}
