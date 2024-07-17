package no.jonasandersen.admin.adapter.out.ssh.jsch;

import com.jcraft.jsch.JSchException;
import java.util.Properties;

public sealed interface SessionWrapper permits RealSSH, StubSSH {

  void connect() throws JSchException;

  void disconnect();

  ChannelExecWrapper openChannel(String type) throws JSchException;

  void setPassword(String password);

  void setConfig(Properties config);

  boolean isConnected();
}
