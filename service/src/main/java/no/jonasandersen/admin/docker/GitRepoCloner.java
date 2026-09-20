package no.jonasandersen.admin.docker;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitRepoCloner {

  private static final Logger log = LoggerFactory.getLogger(GitRepoCloner.class);
  private Git git;

  //  @EventListener(ApplicationStartedEvent.class)
  public File init(boolean skipGit) throws Exception {

    String gitOrg = "jonasandersen-no";
    String gitRepo = "portainer-docker-files";
    String repo = gitOrg + "/" + gitRepo;

    File localDir = new File("/tmp/" + gitRepo);

    if (!skipGit) {
      if (!localDir.exists()) {
        log.info("Cloning {} repository", repo);
        git =
            Git.cloneRepository().setURI("https://github.com/" + repo).setDirectory(localDir).call();
      } else {
        log.info("Pulling from master");
        git = Git.open(localDir);
        git.pull().call();
      }
    }
    return localDir;
  }
}
