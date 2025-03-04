package no.jonasandersen.admin.adapter.out.ssh.jsch;

import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StubChannelExec implements ChannelExecWrapper {

  private static final Logger log = LoggerFactory.getLogger(StubChannelExec.class);

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
  public int getExitStatus() {
    log.info("StubChannelExec: getExitStatus()");
    return 0;
  }

  @Override
  public void setErrStream(OutputStream errStream) {
    log.info("StubChannelExec: setErrStream({})", errStream);
  }

  @Override
  public InputStream getInputStream() {
    log.info("StubChannelExec: getInputStream()");
    return InputStream.nullInputStream();
  }
}
