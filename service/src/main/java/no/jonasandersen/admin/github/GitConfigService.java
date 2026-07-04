package no.jonasandersen.admin.github;

import java.io.File;
import java.nio.file.Path;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class GitConfigService {

  private static final Logger log = LoggerFactory.getLogger(GitConfigService.class);
  private Git git;
  private final File localDir = new File("/tmp/jonasandersen-no/commands");

  @EventListener(ApplicationStartedEvent.class)
  public void init() throws Exception {
    if (!localDir.exists()) {
      log.info("Cloning commands repository");
      git =
          Git.cloneRepository()
              .setURI("https://github.com/jonasandersen-no/commands.git")
              .setDirectory(localDir)
              .call();
    } else {
      log.info("Pulling from master");
      git = Git.open(localDir);
      git.pull().call();
    }
  }

  @Scheduled(initialDelay = 300_300, fixedDelay = 300_000) // 5 minutes
  public void refresh() throws Exception {
    git.pull().call();
  }

  public Path resolve(String relativePath) {
    return localDir.toPath().resolve(relativePath);
  }
}
