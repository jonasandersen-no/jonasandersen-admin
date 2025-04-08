package no.jonasandersen.admin.adapter.out.ssh;

import com.jcraft.jsch.JSchException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import no.jonasandersen.admin.domain.CommandExecutionFailedException;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.domain.PasswordConnectionInfo;
import no.jonasandersen.admin.infrastructure.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileExecutor {

  private static final Logger log = LoggerFactory.getLogger(FileExecutor.class);
  private CommandExecutor commandExecutor;

  private FileExecutor(CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
  }

  public static FileExecutor create() {
    return new FileExecutor(null);
  }

  public static FileExecutor createNull() {
    return new FileExecutor(CommandExecutor.createNull());
  }

  public void setup(PasswordConnectionInfo connectionInfo) throws JSchException {
    if (commandExecutor == null) {
      if (Features.isEnabled(Feature.LINODE_STUB)) {
        commandExecutor = CommandExecutor.createNull();
      } else {
        commandExecutor = CommandExecutor.create(connectionInfo);
      }
    }
  }

  public void cleanup() {
    if (hasCommandExecutor()) {
      commandExecutor = null;
    }
  }

  public boolean hasCommandExecutor() {
    return commandExecutor != null;
  }

  public void parseFile(String file) throws IOException, InterruptedException {
    if (!hasCommandExecutor()) {
      throw new IllegalStateException("CommandExecutor not set");
    }

    Path path = getPath(file);

    try (Stream<String> lines = Files.lines(path)) {
      commandExecutor.connect();

      List<String> list = lines.toList();

      for (String line : list) {
        try {
          commandExecutor.executeCommand(line);
        } catch (JSchException | IOException e) {
          throw new CommandExecutionFailedException(e);
        } catch (InterruptedException e) {
          log.error("Interrupted while executing command: {}", line);
          throw e;
        }
      }
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
