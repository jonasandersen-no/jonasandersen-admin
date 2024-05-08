package no.jonasandersen.admin.adapter.out.ssh;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcraft.jsch.JSchException;
import java.io.FileNotFoundException;
import java.io.IOException;
import no.jonasandersen.admin.OutputTracker;
import org.junit.jupiter.api.Test;

class FileExecutorTest {

  @Test
  void commandsExecutedWhenFileIsParsed() throws JSchException, IOException {
    FileExecutor executor = FileExecutor.createNull();

    OutputTracker<String> tracker = executor.getCommandExecutor().trackOutput();

    executor.parseFile("test.txt");

    assertThat(tracker.data())
        .containsExactly("echo \" Hello World\" >> file.txt");
  }

  @Test
  void fileNotFoundExceptionWhenFileIsNotFound() throws JSchException {
    FileExecutor executor = FileExecutor.createNull();

    assertThatThrownBy(() -> executor.getPath("this_file_does_not_exist.txt"))
        .isInstanceOf(FileNotFoundException.class)
        .hasMessage("File not found");
  }
}