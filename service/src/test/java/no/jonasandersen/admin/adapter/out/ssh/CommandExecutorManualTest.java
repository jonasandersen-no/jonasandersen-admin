package no.jonasandersen.admin.adapter.out.ssh;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.PasswordConnectionInfo;
import no.jonasandersen.admin.domain.SensitiveString;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("manual")
class CommandExecutorManualTest {

  @Test
  void testRealServer() throws JSchException, IOException, InterruptedException {

    CommandExecutor service = CommandExecutor.create(
        new PasswordConnectionInfo("gollien", SensitiveString.of("Jonas123"), new Ip("172.18.131.214"), 22));

    OutputTracker<String> tracker = service.trackOutput();

    service.connect();
    service.executeCommand("echo \" Hello World\" >> file.txt");
    service.disconnect();
    assertThat(tracker.data())
        .containsExactly("echo \" Hello World\" >> file.txt");
  }
}