package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcraft.jsch.JSchException;
import java.util.Collections;
import java.util.List;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.adapter.out.ssh.FileExecutor;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerGeneratorResponse;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.domain.Username;
import no.jonasandersen.admin.infrastructure.AdminProperties.Linode;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class ServerGeneratorTest {

  @Test
  void createInstanceWhenCalled() {
    ServerGenerator generator = ServerGenerator.createNull();

    var response = generator.generate("owner", ServerType.MINECRAFT);

    assertThat(response).isNotNull();
    assertThat(response.ip()).isEqualTo(new Ip("127.0.0.1"));
    assertThat(response.label()).startsWith("minecraft-auto-config-");
  }

  @Test
  void passwordIsSetWhenCallingGenerateWithPasswordProvided() {
    ServerGenerator generator = ServerGenerator.createNull();

    OutputTracker<SensitiveString> tracker = generator.passwordTracker();

    generator.generate("generated", SensitiveString.of("providedPassword"),
        ServerType.MINECRAFT);

    assertThat(tracker.data().getFirst().value()).isEqualTo("providedPassword");
  }

  @Test
  void defaultPasswordIsUsedWhenCallingGenerateWithoutPasswordProvided() {
    ServerGenerator generator = ServerGenerator.createNull();

    OutputTracker<SensitiveString> tracker = generator.passwordTracker();

    generator.generate("owner", ServerType.MINECRAFT);

    assertThat(tracker.data().getFirst().value()).isEqualTo("Password123!");
  }

  @Test
  void serverResponseContainsTags() throws JSchException {
    LinodeInstanceApi instanceApi = Instancio.of(LinodeInstanceApi.class)
        .set(Select.field(LinodeInstanceApi::tags), List.of("auto-created"))
        .set(Select.field(LinodeInstanceApi::ipv4), List.of("127.0.0.1"))
        .create();

    LinodeService service = LinodeService.createNull(List.of(instanceApi), Collections.emptyList());

    FileExecutor fileExecutor = FileExecutor.createNull();
    ServerGenerator generator = ServerGenerator.create(
        service,
        SensitiveString.of("password"),
        fileExecutor,
        DnsService.configureForTest(),
        DeleteLinodeInstance.configureForTest(), new Linode("base", "token", "password", 1L),
        LinodeVolumeService.createNull());

    ServerGeneratorResponse response = generator.generate(Username.create("someUsername"), ServerType.MINECRAFT);

    assertThat(response.tags()).contains("owner:someUsername", "auto-created");
  }
}