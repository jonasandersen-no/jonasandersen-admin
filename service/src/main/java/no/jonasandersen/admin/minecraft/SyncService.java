package no.jonasandersen.admin.minecraft;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.util.stream.Stream;
import no.jonasandersen.admin.ssh.FileExecutor;
import org.springframework.stereotype.Service;

@Service
class SyncService {

  public static final String COMMAND =
      "rsync --delete -av --exclude-from='exclude-andersen.txt' /root/andersen/ gollien@mira2:/mnt/nas/public/backups/minecraft/andersen";
  private final FileExecutor fileExecutor;

  public SyncService(FileExecutor minecraftFileExecutor) {
    this.fileExecutor = minecraftFileExecutor;
  }

  public void sync() throws IOException, InterruptedException, JSchException {
    fileExecutor.runLines(Stream.of(COMMAND));
  }
}
