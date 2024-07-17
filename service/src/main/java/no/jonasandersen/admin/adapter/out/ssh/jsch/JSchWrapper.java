package no.jonasandersen.admin.adapter.out.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

public sealed interface JSchWrapper permits RealJSchWrapper, StubJSchWrapper {

  SessionWrapper getSession(String username, String host, int port) throws JSchException;

  JSch getJSch();

  void addIdentity(String privateKey) throws JSchException;

  void removeAllIdentity() throws JSchException;
}
