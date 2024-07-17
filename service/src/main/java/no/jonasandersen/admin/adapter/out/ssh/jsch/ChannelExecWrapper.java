package no.jonasandersen.admin.adapter.out.ssh.jsch;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public sealed interface ChannelExecWrapper permits RealChannelExec, StubChannelExec {

  void setCommand(String command);

  void connect() throws JSchException;

  void disconnect();

  boolean isClosed();

  void setErrStream(OutputStream errStream);

  InputStream getInputStream() throws IOException;
}
