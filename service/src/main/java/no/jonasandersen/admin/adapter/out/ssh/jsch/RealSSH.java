package no.jonasandersen.admin.adapter.out.ssh.jsch;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.util.Properties;

public final class RealSSH implements SessionWrapper {

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
