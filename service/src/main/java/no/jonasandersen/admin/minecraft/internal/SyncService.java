package no.jonasandersen.admin.minecraft.internal;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.util.stream.Stream;
import no.jonasandersen.admin.domain.ConnectionInfo;
import no.jonasandersen.admin.domain.Hostname;
import no.jonasandersen.admin.domain.PasswordConnectionInfo;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import no.jonasandersen.admin.infrastructure.AdminProperties.Minecraft.Andersen;
import no.jonasandersen.admin.ssh.FileExecutor;
import org.springframework.stereotype.Service;

@Service
class SyncService {

  public static final String COMMAND =
      "rsync --delete -av --exclude-from='exclude-andersen.txt' /root/andersen/ gollien@mira2:/mnt/nas/public/backups/minecraft/andersen";

  private final AdminProperties properties;

  public SyncService(AdminProperties properties) {
    this.properties = properties;
  }

  public void sync() throws IOException, InterruptedException, JSchException {
    Andersen andersen = properties.minecraft().andersen();

    FileExecutor fileExecutor = FileExecutor.create();

    ConnectionInfo info =
        new PasswordConnectionInfo(
            andersen.username(),
            SensitiveString.of(andersen.password()),
            new Hostname(andersen.hostname()),
            22);

    fileExecutor.setup(info);
    fileExecutor.runLines(Stream.of(COMMAND));
    fileExecutor.cleanup();
  }
}
