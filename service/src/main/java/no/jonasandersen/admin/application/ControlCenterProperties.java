package no.jonasandersen.admin.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import no.jonasandersen.admin.domain.SensitiveString;

public record ControlCenterProperties(String username, String privateKeyFilePath, String ip, Integer port) {

  public static ControlCenterProperties withKey(String username, SensitiveString privateKeyFile, String ip,
      Integer port) {
    File tempFile;
    try {
      tempFile = Files.createTempFile("privateKey", ".pem").toFile();
      try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
        fileOutputStream.write(privateKeyFile.value().getBytes());
      }
      tempFile.setReadable(true, true);
      tempFile.setWritable(false);
      tempFile.setExecutable(true, true);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new ControlCenterProperties(username, tempFile.getAbsolutePath(), ip, port);
  }

  public static ControlCenterProperties configureForTest() {
    return new ControlCenterProperties("test", "", "127.0.0.1", 22);
  }
}
