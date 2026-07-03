package no.jonasandersen.admin.minecraft; // Source - https://stackoverflow.com/a/59289769
import static java.util.Arrays.asList;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

class RunCommandViaSsh {

  private static final String SSH_HOST = "bgg-minecraft";
  private static final String SSH_LOGIN = "root";
  private static final String SSH_PASSWORD = "Jonas123";

  static void main() throws JSchException, IOException {
    System.out.println(runCommand("pwd"));
    System.out.println(runCommand("ls -la"));
  }

  private static List<String> runCommand(String command) throws JSchException, IOException {
    Session session = setupSshSession();
    session.connect();

    ChannelExec channel = (ChannelExec) session.openChannel("exec");
    try {
      channel.setCommand(command);
      channel.setInputStream(null);
      String result;
      try (InputStream output = channel.getInputStream()) {
        channel.connect();

        try (InputStreamReader inputStreamReader = new InputStreamReader(output)) {
          result = inputStreamReader.readAllAsString();
        }
      }
      return asList(result.split("\n"));

    } catch (JSchException | IOException e) {
      closeConnection(channel, session);
      throw new RuntimeException(e);

    } finally {
      closeConnection(channel, session);
    }
  }

  private static Session setupSshSession() throws JSchException {
    Session session = new JSch().getSession(SSH_LOGIN, SSH_HOST, 22);
    session.setPassword(SSH_PASSWORD.getBytes());
    session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
    session.setConfig("StrictHostKeyChecking", "no"); // disable check for RSA key
    return session;
  }

  private static void closeConnection(ChannelExec channel, Session session) {
    try {
      channel.disconnect();
    } catch (Exception ignored) {
    }
    session.disconnect();
  }
}
