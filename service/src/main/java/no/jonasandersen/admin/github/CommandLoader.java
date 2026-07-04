package no.jonasandersen.admin.github;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class CommandLoader {

  private final GitConfigService gitConfigService;
  private final YAMLMapper mapper;

  public CommandLoader(GitConfigService gitConfigService) {
    this.gitConfigService = gitConfigService;
    this.mapper = new YAMLMapper();
  }

  public CommandConfig load(String commandName) throws IOException {
    Path path = gitConfigService.resolve("commands/" + commandName + ".yaml");

    if (!Files.exists(path)) {
      throw new FileNotFoundException("Command file not found: " + path);
    }

    CommandConfig cfg = mapper.readValue(path.toFile(), CommandConfig.class);
    validateTopLevel(cfg);
    validateSpec(cfg);
    return cfg;
  }

  private void validateTopLevel(CommandConfig cfg) {
    if (cfg.getApiVersion() == null) {
      throw new IllegalArgumentException("apiVersion is required");
    }
    if (cfg.getKind() == null) {
      throw new IllegalArgumentException("kind is required");
    }
    if (cfg.getMetadata() == null || cfg.getMetadata().getName() == null) {
      throw new IllegalArgumentException("metadata.name is required");
    }
    if (cfg.getType() == null) {
      throw new IllegalArgumentException("type is required");
    }
    if (cfg.getSpec() == null) {
      throw new IllegalArgumentException("spec section is required");
    }
  }

  private void validateSpec(CommandConfig cfg) {
    switch (cfg.getType()) {
      case "rsync":
        RsyncSpec rsync = mapper.convertValue(cfg.getSpec(), RsyncSpec.class);
        if (rsync.getSource() == null || rsync.getDestination() == null) {
          throw new IllegalArgumentException("rsync requires source + destination");
        }
        break;
      case "test":
        TestSpec test = mapper.convertValue(cfg.getSpec(), TestSpec.class);
        if (test.field() == null) {
          throw new IllegalArgumentException("test requires field");
        }
        break;

      //      case "ssh":
      //        SshSpec ssh = mapper.convertValue(cfg.getSpec(), SshSpec.class);
      //        if (ssh.getCommand() == null) {
      //          throw new IllegalArgumentException("ssh requires command");
      //        }
      //        break;
      //
      //      case "docker":
      //        DockerSpec docker = mapper.convertValue(cfg.getSpec(), DockerSpec.class);
      //        if (docker.getAction() == null) {
      //          throw new IllegalArgumentException("docker requires action");
      //        }
      //        break;
      //
      //      case "scp":
      //        ScpSpec scp = mapper.convertValue(cfg.getSpec(), ScpSpec.class);
      //        if (scp.getScpSource() == null || scp.getScpDestination() == null) {
      //          throw new IllegalArgumentException("scp requires scpSource + scpDestination");
      //        }
      //        break;
      //
      //      case "systemd":
      //        SystemdSpec systemd = mapper.convertValue(cfg.getSpec(), SystemdSpec.class);
      //        if (systemd.getService() == null || systemd.getSystemdAction() == null) {
      //          throw new IllegalArgumentException("systemd requires service + systemdAction");
      //        }
      //        break;
      //
      //      case "custom":
      //        CustomSpec custom = mapper.convertValue(cfg.getSpec(), CustomSpec.class);
      //        if (custom.getExec() == null || custom.getExec().isEmpty()) {
      //          throw new IllegalArgumentException("custom requires exec list");
      //        }
      //        break;

      default:
        throw new IllegalArgumentException("Unknown command type: " + cfg.getType());
    }
  }
}
