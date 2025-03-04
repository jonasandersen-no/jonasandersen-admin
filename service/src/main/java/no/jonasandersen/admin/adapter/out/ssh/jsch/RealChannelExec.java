package no.jonasandersen.admin.adapter.out.ssh.jsch;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class RealChannelExec implements ChannelExecWrapper {

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
  public int getExitStatus() {
    return channel.getExitStatus();
  }

  @Override
  public void setErrStream(OutputStream errStream) {
    channel.setErrStream(errStream);
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return channel.getInputStream();
  }
}
