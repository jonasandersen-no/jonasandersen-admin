package no.jonasandersen.admin.github;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommandLoader {

  private static final Logger log = LoggerFactory.getLogger(CommandLoader.class);
  private final GitConfigService gitConfigService;
  private final YAMLMapper mapper;

  private final @NonNull Cache cache;

  public CommandLoader(GitConfigService gitConfigService, CacheManager cacheManager) {
    this.gitConfigService = gitConfigService;
    cache = Objects.requireNonNull(cacheManager.getCache("commands"));

    this.mapper = new YAMLMapper();
  }

  public Page<CommandConfig> allCommands() {
    List<CommandConfig> commands =
        gitConfigService.allCommands().stream()
            .map(
                commandName -> {
                  try {
                    return load(commandName);
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                })
            .toList();
    return new PageImpl<>(commands, Pageable.unpaged(), commands.size());
  }

  public CommandConfig load(String commandName) throws IOException {
    CommandConfig config = cache.get(commandName, CommandConfig.class);

    if (config != null) {
      log.info("Found in cache {}", config);
      return config;
    }

    Path path = gitConfigService.resolve("commands/" + commandName + ".yaml");

    if (!Files.exists(path)) {
      throw new FileNotFoundException("Command file not found: " + path);
    }

    CommandConfig cfg = mapper.readValue(path.toFile(), CommandConfig.class);
    validateTopLevel(cfg);
    validateSpec(cfg);

    cache.put(commandName, cfg);
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
