package no.jonasandersen.admin.commands;

import java.io.IOException;
import no.jonasandersen.admin.github.CommandConfig;
import no.jonasandersen.admin.github.CommandLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commands")
public class CommandsRestController {

  private final CommandLoader commandLoader;
  private final CommandAssembler commandAssembler;
  private final PagedResourcesAssembler<Command> pagedResourcesAssembler;

  public CommandsRestController(
      CommandLoader commandLoader,
      CommandAssembler commandAssembler,
      PagedResourcesAssembler<Command> pagedResourcesAssembler) {
    this.commandLoader = commandLoader;
    this.commandAssembler = commandAssembler;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }

  @GetMapping("/{name}")
  public EntityModel<Command> command(@PathVariable String name) throws IOException {
    CommandConfig config = commandLoader.load(name);

    Command command = new Command(config.getMetadata().getName());

    return commandAssembler.toModel(command);
  }

  @GetMapping
  PagedModel<EntityModel<Command>> commands() {
    Page<Command> commands = commandLoader.allCommands().map(Command::new);

    return pagedResourcesAssembler.toModel(commands, commandAssembler);
  }
}
