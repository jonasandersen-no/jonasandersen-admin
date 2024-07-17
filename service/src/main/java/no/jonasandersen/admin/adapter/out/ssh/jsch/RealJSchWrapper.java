package no.jonasandersen.admin.adapter.out.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

public final class RealJSchWrapper implements JSchWrapper {

  private final JSch jsch = new JSch();

  @Override
  public RealSSH getSession(String username, String host, int port) throws JSchException {
    return new RealSSH(this, username, host, port);
  }

  @Override
  public JSch getJSch() {
    return jsch;
  }

  @Override
  public void addIdentity(String privateKey) throws JSchException {
    jsch.addIdentity(privateKey);
  }

  @Override
  public void removeAllIdentity() throws JSchException {
    jsch.removeAllIdentity();
  }
}
