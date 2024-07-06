package no.jonasandersen.admin.adapter.out.ssh;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import no.jonasandersen.admin.OutputTracker;
import org.junit.jupiter.api.Test;

class CommandExecutorTest {

  @Test
  void outputTrackerTracksEchoHelloWorldWhenCommandIsExecuted()
      throws JSchException, IOException, InterruptedException {
    CommandExecutor executor = CommandExecutor.createNull();
    OutputTracker<String> tracker = executor.trackOutput();

    executor.connect();
    executor.executeCommand("echo \" Hello World\" >> file.txt");
    executor.disconnect();

    assertThat(tracker.data())
        .containsExactly("echo \" Hello World\" >> file.txt");
  }

  @Test
  void illegalStateExceptionWhenSessionIsNotConnected() {
    CommandExecutor service = CommandExecutor.createNull();

    assertThatThrownBy(() -> service.executeCommand("echo \" Hello World\" >> file.txt"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Session is not connected");
  }
}