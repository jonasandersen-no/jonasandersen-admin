package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.core.minecraft.domain.Ip;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerType;
import org.junit.jupiter.api.Test;

class ServerGeneratorTest {

  @Test
  void createInstanceWhenCalled() {
    ServerGenerator generator = ServerGenerator.createNull();

    var response = generator.generate(ServerType.MINECRAFT);

    assertThat(response).isNotNull();
    assertThat(response.ip()).isEqualTo(new Ip("127.0.0.1"));
    assertThat(response.label()).startsWith("minecraft-auto-config-");
  }

  @Test
  void passwordIsSetWhenCallingGenerateWithPasswordProvided() {
    ServerGenerator generator = ServerGenerator.createNull();

    OutputTracker<SensitiveString> tracker = generator.passwordTracker();

    generator.generate(ServerType.MINECRAFT, SensitiveString.of("providedPassword"));

    assertThat(tracker.data().getFirst().value()).isEqualTo("providedPassword");
  }

  @Test
  void defaultPasswordIsUsedWhenCallingGenerateWithoutPasswordProvided() {
    ServerGenerator generator = ServerGenerator.createNull();

    OutputTracker<SensitiveString> tracker = generator.passwordTracker();

    generator.generate(ServerType.MINECRAFT);

    assertThat(tracker.data().getFirst().value()).isEqualTo("Password123!");
  }
}