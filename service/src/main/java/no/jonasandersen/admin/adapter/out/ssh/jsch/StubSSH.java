package no.jonasandersen.admin.adapter.out.ssh.jsch;

import com.jcraft.jsch.JSchException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StubSSH implements SessionWrapper {

  private static final Logger log = LoggerFactory.getLogger(StubSSH.class);
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
