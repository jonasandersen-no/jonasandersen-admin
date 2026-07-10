package no.jonasandersen.admin.commands;

import java.io.IOException;
import no.jonasandersen.admin.github.CommandConfig;
import no.jonasandersen.admin.github.CommandLoader;
import no.jonasandersen.admin.github.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commands")
public class CommandsRestController {

  private final CommandLoader commandLoader;
  private final CommandAssembler commandAssembler;
  private final PagedResourcesAssembler<CommandConfig> pagedResourcesAssembler;

  public CommandsRestController(
      CommandLoader commandLoader,
      CommandAssembler commandAssembler,
      PagedResourcesAssembler<CommandConfig> pagedResourcesAssembler) {
    this.commandLoader = commandLoader;
    this.commandAssembler = commandAssembler;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }

  @GetMapping("/{name}")
  public EntityModel<CommandConfig> command(@PathVariable String name) throws IOException {
    CommandConfig config = commandLoader.load(name);

    return commandAssembler.toModel(config);
  }

  @GetMapping
  PagedModel<EntityModel<CommandConfig>> commands() {
    Page<CommandConfig> commands = commandLoader.allCommands();

    return pagedResourcesAssembler.toModel(commands, commandAssembler);
  }

  @DeleteMapping("/{name}")
  ResponseEntity<Void> delete(@PathVariable String name) {
    return ResponseEntity.ok(null);
  }
}
