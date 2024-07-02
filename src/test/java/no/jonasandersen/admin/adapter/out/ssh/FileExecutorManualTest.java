package no.jonasandersen.admin.adapter.out.ssh;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import no.jonasandersen.admin.OutputTracker;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("manual")
class FileExecutorManualTest {

  @Test
  void testRunningFile() throws JSchException, IOException, InterruptedException {

    FileExecutor executor = FileExecutor.createNull();

    OutputTracker<String> tracker = executor.getCommandExecutor().trackOutput();

    executor.parseFile("install-docker.txt");

    assertThat(tracker.data())
        .containsExactly("echo \" Hello World\" >> file.txt");
  }
}