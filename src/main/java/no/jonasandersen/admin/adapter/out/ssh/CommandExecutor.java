package no.jonasandersen.admin.adapter.out.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.core.domain.ConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandExecutor {

  private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);
  private final OutputListener<String> outputListener = new OutputListener<>();

  private final JSchWrapper jsch;
  private SessionWrapper session;


  public static CommandExecutor create(ConnectionInfo connectionInfo) throws JSchException {
    return new CommandExecutor(new RealJSchWrapper(), connectionInfo);
  }

  public static CommandExecutor createNull() throws JSchException {
    return new CommandExecutor(new StubJSchWrapper(), ConnectionInfo.createNull());
  }

  private CommandExecutor(JSchWrapper jschWrapper, ConnectionInfo connectionInfo) throws JSchException {
    this.jsch = jschWrapper;
    setupConnection(connectionInfo);
  }

  public OutputTracker<String> trackOutput() {
    return outputListener.createTracker();
  }

  public void connect() throws JSchException {
    session.connect();
  }

  public void disconnect() {
    session.disconnect();
  }

  public void executeCommand(String command) throws JSchException, IOException {
    if (session == null || !session.isConnected()) {
      throw new IllegalStateException("Session is not connected");
    }

    ChannelExecWrapper exec = session.openChannel("exec");
    try {
      exec.setCommand(command);
      exec.connect();
      log.info("Running command {}", command);
      String channelOutput = getChannelOutput(exec, exec.getInputStream());
      log.info("Command output: {}", channelOutput);
      outputListener.track(command);
    } finally {
      exec.disconnect();
    }
  }

  private void setupConnection(ConnectionInfo connectionInfo) throws JSchException {
    log.info("Connecting to {}", connectionInfo);
    session = jsch.getSession(connectionInfo.username(), connectionInfo.ip().value(),
        connectionInfo.port());
    session.setPassword(connectionInfo.password().value());

    Properties config = new Properties();
    config.put("StrictHostKeyChecking", "no");
    session.setConfig(config);
  }


  private String getChannelOutput(ChannelExecWrapper channel, InputStream in) throws IOException {

    byte[] buffer = new byte[1024];
    StringBuilder strBuilder = new StringBuilder();

    String line = "";
    while (true) {
      while (in.available() > 0) {
        int i = in.read(buffer, 0, 1024);
        if (i < 0) {
          break;
        }
        strBuilder.append(new String(buffer, 0, i));
        System.out.println(line);
      }

      if (line.contains("logout")) {
        break;
      }

      if (channel.isClosed()) {
        break;
      }
      try {
        Thread.sleep(1000);
      } catch (Exception ee) {
      }
    }

    return strBuilder.toString();
  }

  private interface JSchWrapper {

    SessionWrapper getSession(String username, String host, int port) throws JSchException;

    JSch getJSch();
  }

  private static class RealJSchWrapper implements JSchWrapper {

    private final JSch jsch = new JSch();


    @Override
    public RealSSH getSession(String username, String host, int port) throws JSchException {
      return new RealSSH(this, username, host, port);
    }

    @Override
    public JSch getJSch() {
      return jsch;
    }
  }

  private static class StubJSchWrapper implements JSchWrapper {

    @Override
    public SessionWrapper getSession(String username, String host, int port) {
      return new StubSSH();
    }

    @Override
    public JSch getJSch() {
      return null;
    }
  }


  private interface SessionWrapper {

    void connect() throws JSchException;

    void disconnect();

    ChannelExecWrapper openChannel(String type) throws JSchException;

    void setPassword(String password);

    void setConfig(Properties config);

    boolean isConnected();
  }

  private static class RealSSH implements SessionWrapper {

    private final Session session;

    public RealSSH(JSchWrapper jsch, String username, String host, int port) throws JSchException {
      session = jsch.getJSch().getSession(username, host, port);
    }

    @Override
    public void connect() throws JSchException {
      session.connect();
    }

    @Override
    public void disconnect() {
      session.disconnect();
    }

    @Override
    public ChannelExecWrapper openChannel(String type) throws JSchException {
      return new RealChannelExec(session, type);
    }

    @Override
    public void setPassword(String password) {
      session.setPassword(password);
    }

    @Override
    public void setConfig(Properties config) {
      session.setConfig(config);
    }

    @Override
    public boolean isConnected() {
      return session.isConnected();
    }
  }

  private static class StubSSH implements SessionWrapper {

    private boolean connected;

    @Override
    public void connect() {
      log.info("StubSSH: connect()");
      connected = true;
    }

    @Override
    public void disconnect() {
      log.info("StubSSH: disconnect()");
      connected = false;
    }

    @Override
    public ChannelExecWrapper openChannel(String type) throws JSchException {
      return new StubChannelExec();
    }

    @Override
    public void setPassword(String password) {
      log.info("StubSSH: setPassword {}", password);
    }

    @Override
    public void setConfig(Properties config) {
      log.info("StubSSH: setConfig({})", config);
    }

    @Override
    public boolean isConnected() {
      return connected;
    }
  }

  private interface ChannelExecWrapper {


    void setCommand(String command);

    void connect() throws JSchException;

    void disconnect();

    boolean isClosed();

    InputStream getInputStream() throws IOException;
  }

  private static class RealChannelExec implements ChannelExecWrapper {

    private final ChannelExec channel;

    public RealChannelExec(Session session, String type) throws JSchException {
      channel = (ChannelExec) session.openChannel(type);
    }

    @Override
    public void setCommand(String command) {
      channel.setCommand(command);
    }

    @Override
    public void connect() throws JSchException {
      channel.connect();
    }

    @Override
    public void disconnect() {
      channel.disconnect();
    }

    @Override
    public boolean isClosed() {
      return channel.isClosed();
    }

    @Override
    public InputStream getInputStream() throws IOException {
      return channel.getInputStream();
    }
  }

  private static class StubChannelExec implements ChannelExecWrapper {

    @Override
    public void setCommand(String command) {
      log.info("StubChannelExec: setCommand({})", command);
    }

    @Override
    public void connect() {
      log.info("StubChannelExec: connect()");
    }

    @Override
    public void disconnect() {
      log.info("StubChannelExec: disconnect()");
    }

    @Override
    public boolean isClosed() {
      log.info("StubChannelExec: isClosed()");
      return true;
    }

    @Override
    public InputStream getInputStream() {
      log.info("StubChannelExec: getInputStream()");
      return InputStream.nullInputStream();
    }
  }
}
