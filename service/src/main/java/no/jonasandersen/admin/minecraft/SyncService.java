package no.jonasandersen.admin.minecraft;

import java.io.IOException;
import no.jonasandersen.admin.ssh.FileExecutor;
import org.springframework.stereotype.Service;

@Service
class SyncService {

  private final FileExecutor fileExecutor;

  public SyncService(FileExecutor minecraftFileExecutor) {
    this.fileExecutor = minecraftFileExecutor;
  }

  public void sync() throws IOException, InterruptedException {

    fileExecutor.parseFile("rsync-minecraft-andersen.txt");
  }
}
