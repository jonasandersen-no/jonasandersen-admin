package no.jonasandersen.admin.adapter.out.ssh;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.adapter.out.ssh.jsch.ChannelExecWrapper;
import no.jonasandersen.admin.adapter.out.ssh.jsch.JSchWrapper;
import no.jonasandersen.admin.adapter.out.ssh.jsch.RealJSchWrapper;
import no.jonasandersen.admin.adapter.out.ssh.jsch.SessionWrapper;
import no.jonasandersen.admin.adapter.out.ssh.jsch.StubJSchWrapper;
import no.jonasandersen.admin.domain.ConnectionInfo;
import no.jonasandersen.admin.domain.PasswordConnectionInfo;
import no.jonasandersen.admin.domain.PrivateKeyConnectionInfo;
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

  private CommandExecutor(JSchWrapper jschWrapper, ConnectionInfo connectionInfo) throws JSchException {
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

  public String executeCommand(String command) throws JSchException, IOException, InterruptedException {
    if (session == null || !session.isConnected()) {
      throw new IllegalStateException("Session is not connected");
    }

    ChannelExecWrapper exec = session.openChannel("exec");
    try {
      exec.setCommand(command);
      exec.connect();
      exec.setErrStream(System.err);
      log.info("Running command {}", command);
      String channelOutput = getChannelOutput(exec, exec.getInputStream());
      log.info("Command output: {}", channelOutput);
      outputListener.track(command);
      return channelOutput;
    } finally {
      exec.disconnect();
    }
  }

  public void setupConnection(ConnectionInfo connectionInfo) throws JSchException {
    log.info("Connecting to {}", connectionInfo);
    switch (connectionInfo) {
      case PasswordConnectionInfo password -> {
        session = jsch.getSession(password.username(), password.ip().value(),
            password.port());
        session.setPassword(password.credentials().value());
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
      }
      case PrivateKeyConnectionInfo privateKey -> {
        jsch.addIdentity(privateKey.credentials().value());
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session = jsch.getSession(privateKey.username(), privateKey.ip().value(),
            privateKey.port());
        session.setConfig(config);
      }
    }
  }


  private String getChannelOutput(ChannelExecWrapper channel, InputStream in) throws IOException, InterruptedException {

    byte[] buffer = new byte[1024];
    StringBuilder strBuilder = new StringBuilder();

    while (!channel.isClosed()) {
      while (in.available() > 0) {
        int i = in.read(buffer, 0, 1024);
        if (i < 0) {
          break;
        }
        String line = new String(buffer, 0, i);
        strBuilder.append(line);
        log.info("{}{}", System.lineSeparator(), line);
      }
      Thread.sleep(1000);
    }

    return strBuilder.toString();
  }

}
