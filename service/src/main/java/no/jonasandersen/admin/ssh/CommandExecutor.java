package no.jonasandersen.admin.ssh;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.domain.ConnectionInfo;
import no.jonasandersen.admin.domain.PasswordConnectionInfo;
import no.jonasandersen.admin.domain.PrivateKeyConnectionInfo;
import no.jonasandersen.admin.ssh.jsch.ChannelExecWrapper;
import no.jonasandersen.admin.ssh.jsch.JSchWrapper;
import no.jonasandersen.admin.ssh.jsch.RealJSchWrapper;
import no.jonasandersen.admin.ssh.jsch.SessionWrapper;
import no.jonasandersen.admin.ssh.jsch.StubJSchWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandExecutor {

  private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);
  private final OutputListener<String> outputListener = new OutputListener<>();

  private final JSchWrapper jsch;
  private SessionWrapper session;

  public static CommandExecutor create() throws JSchException {
    return new CommandExecutor(new RealJSchWrapper(), null);
  }

  public static CommandExecutor create(ConnectionInfo connectionInfo) throws JSchException {
    return new CommandExecutor(new RealJSchWrapper(), connectionInfo);
  }

  public static CommandExecutor createNull() {
    try {
      return new CommandExecutor(new StubJSchWrapper(), PasswordConnectionInfo.createNull());
    } catch (JSchException _) {
      throw new IllegalStateException("Failed to create CommandExecutor");
    }
  }

  private CommandExecutor(JSchWrapper jschWrapper, ConnectionInfo connectionInfo)
      throws JSchException {
    this.jsch = jschWrapper;
    if (connectionInfo != null) {
      setupConnection(connectionInfo);
    }
  }

  public OutputTracker<String> trackOutput() {
    return outputListener.createTracker();
  }

  public void connect() throws JSchException {
    session.connect();
  }

  public void disconnect() throws JSchException {
    jsch.removeAllIdentity();
    session.disconnect();
  }

  public void executeCommand(String command)
      throws JSchException, IOException, InterruptedException {
    if (session == null || !session.isConnected()) {
      throw new IllegalStateException("Session is not connected");
    }

    ChannelExecWrapper exec = session.openChannel("exec");
    try {
      exec.setCommand(command);
      exec.connect();
      exec.setErrStream(System.err);
      log.info("Running command {}", command);
      streamChannelOutput(exec.getInputStream());
      log.info("Done with command");
      outputListener.track(command);
    } finally {
      exec.disconnect();
    }
  }

  public void setupConnection(ConnectionInfo connectionInfo) throws JSchException {
    log.info("Connecting to {}", connectionInfo);
    switch (connectionInfo) {
      case PasswordConnectionInfo password -> {
        session = jsch.getSession(password.username(), password.address().value(), password.port());
        session.setPassword(password.credentials().value());
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
      }
      case PrivateKeyConnectionInfo privateKey -> {
        jsch.addIdentity(privateKey.credentials().value());
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session =
            jsch.getSession(privateKey.username(), privateKey.address().value(), privateKey.port());
        session.setConfig(config);
      }
    }
  }

  private void streamChannelOutput(InputStream in) throws IOException {
    try (InputStreamReader reader = new InputStreamReader(in)) {
      char[] buffer = new char[1024];
      int charsRead;

      // read() blocks until data is available or the stream ends (-1)
      while ((charsRead = reader.read(buffer)) != -1) {
        String chunk = new String(buffer, 0, charsRead);

        System.out.print(chunk);
      }
    }
  }
}
