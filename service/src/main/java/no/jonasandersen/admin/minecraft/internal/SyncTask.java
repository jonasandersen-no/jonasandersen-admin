package no.jonasandersen.admin.minecraft.internal;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class SyncTask {

  private static final Logger log = LoggerFactory.getLogger(SyncTask.class);

  private final SyncService syncService;

  public SyncTask(SyncService syncService) {
    this.syncService = syncService;
  }

  void executeSyncTask(Instant date) {
    log.info("Task is running at {}", date);
    try {
      syncService.sync();
      log.info("Finished with syncing");
    } catch (IOException | InterruptedException | JSchException e) {
      throw new RuntimeException(e);
    }
  }
}
