package no.jonasandersen.admin.adapter.out.ssh;

import com.jcraft.jsch.JSchException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileExecutor {

  private final CommandExecutor commandExecutor;

  private FileExecutor(CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
  }

  public static FileExecutor create(CommandExecutor commandExecutor) {
    return new FileExecutor(commandExecutor);
  }

  public static FileExecutor createNull() throws JSchException {
    return new FileExecutor(CommandExecutor.createNull());
  }

  public void parseFile(String file) throws IOException {
    Path path = getPath(file);

    try (Stream<String> lines = Files.lines(path)) {
      commandExecutor.connect();

      lines.forEach(line -> {
        try {
          commandExecutor.executeCommand(line);
        } catch (JSchException | IOException e) {
          throw new RuntimeException(e);
        }
      });
      commandExecutor.disconnect();
    } catch (JSchException e) {
      throw new IOException(e);
    }

  }

  Path getPath(String file) throws FileNotFoundException {
    URL url = getClass().getClassLoader().getResource(file);

    if (url == null) {
      throw new FileNotFoundException("File not found");
    }

    Path path;
    try {
      path = Paths.get(url.toURI());
    } catch (URISyntaxException e) {
      throw new FileNotFoundException("File not found");
    }
    return path;
  }

  public CommandExecutor getCommandExecutor() {
    return commandExecutor;
  }
}
