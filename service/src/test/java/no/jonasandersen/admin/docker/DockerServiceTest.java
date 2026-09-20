package no.jonasandersen.admin.docker;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import no.jonasandersen.admin.ssh.CommandExecutor;
import org.junit.jupiter.api.Test;

class DockerServiceTest {

  private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");

  @Test
  void run() throws Exception {
    File directory = new GitRepoCloner().init(true);

    List<File> directoriesWithCompose = new ArrayList<>();
    findComposeDirectories(directoriesWithCompose, directory);

    for (File file : directoriesWithCompose) {
      System.out.println("Processing: " + file.getName());

      // Configure OS-specific command
      ProcessBuilder pb = IS_WINDOWS
          ? new ProcessBuilder("cmd.exe", "/c", "dir")
          : new ProcessBuilder("ls", "-la");

      pb.directory(file.getAbsoluteFile());

      int exitCode;
      try (Process process = pb.start()) {

        // Read output stream to prevent process buffering deadlocks
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
          String line;
          while ((line = reader.readLine()) != null) {
            System.out.println(line);
          }
        }

        exitCode = process.waitFor();
      }
      if (exitCode != 0) {
        System.err.println("Command failed with exit code: " + exitCode);
      }
    }  }

  private static void findComposeDirectories(List<File> filesToFind, File directory) {
    File[] children = directory.listFiles();
    if (children == null) return;

    for (File file : children) {
      if (file.getName().startsWith(".")) {
        continue; // Skip hidden folders like .git
      }

      if (file.isDirectory()) {
        File[] subFiles = file.listFiles();
        if (subFiles != null && Arrays.stream(subFiles).anyMatch(f -> f.getName().endsWith(".yaml") || f.getName().endsWith(".yml"))) {
          filesToFind.add(file);
        }
        findComposeDirectories(filesToFind, file);
      }
    }
  }}
