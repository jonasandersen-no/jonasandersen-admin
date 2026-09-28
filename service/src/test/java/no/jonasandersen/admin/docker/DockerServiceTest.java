package no.jonasandersen.admin.docker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DockerServiceTest {

  private static final boolean IS_WINDOWS =
      System.getProperty("os.name").toLowerCase().contains("win");
  private static final Logger log = LoggerFactory.getLogger(DockerServiceTest.class);

  @Test
  void run() throws Exception {
    File directory = new GitRepoCloner().init(false);

    List<File> directoriesWithCompose = new ArrayList<>();
    findComposeDirectories(directoriesWithCompose, directory);

    for (File file : directoriesWithCompose) {
      System.out.println("Processing: " + file.getName());

      // Configure OS-specific command
      ProcessBuilder pb =
          IS_WINDOWS
              ? new ProcessBuilder("cmd.exe", "/c", "dir")
              : new ProcessBuilder("docker", "compose", "pull");

      pb.directory(file.getAbsoluteFile());
      pb.inheritIO();

      Thread shutdownHook = null;
      int exitCode;
      try (Process process = pb.start()) {

        shutdownHook =
            new Thread(
                () -> {
                  if (process.isAlive()) {
                    System.out.println("Terminating lingering process for: " + file.getName());
                    process.destroyForcibly();
                  }
                });
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        exitCode = process.waitFor();
        if (exitCode != 0) {
          System.err.println("Command failed with exit code: " + exitCode);
        }
      } finally {
        if (shutdownHook != null ){
          Runtime.getRuntime().removeShutdownHook(shutdownHook);
        }
      }
    }
  }

  private static void findComposeDirectories(List<File> filesToFind, File directory) {
    File[] children = directory.listFiles();
    if (children == null) return;

    for (File file : children) {
      if (file.getName().startsWith(".")) {
        continue; // Skip hidden folders like .git
      }

      if (file.isDirectory()) {
        File[] subFiles = file.listFiles();
        if (subFiles != null
            && Arrays.stream(subFiles)
                .anyMatch(f -> f.getName().endsWith(".yaml") || f.getName().endsWith(".yml"))) {
          filesToFind.add(file);
        }
        findComposeDirectories(filesToFind, file);
      }
    }
  }
}
