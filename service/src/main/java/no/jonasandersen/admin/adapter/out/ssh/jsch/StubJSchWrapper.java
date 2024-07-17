package no.jonasandersen.admin.adapter.out.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StubJSchWrapper implements JSchWrapper {

  private static final Logger log = LoggerFactory.getLogger(StubJSchWrapper.class);

  @Override
  public SessionWrapper getSession(String username, String host, int port) {
    return new StubSSH();
  }

  @Override
  public JSch getJSch() {
    return new JSch();
  }

  @Override
  public void addIdentity(String privateKey) throws JSchException {
    log.info("StubJSchWrapper: addIdentity({})", privateKey);
  }

  @Override
  public void removeAllIdentity() throws JSchException {
    log.info("StubJSchWrapper: removeAllIdentity()");
  }
}
