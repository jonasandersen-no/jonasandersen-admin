package no.jonasandersen.admin.commands;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import no.jonasandersen.admin.github.CommandConfig;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CommandAssembler
    implements RepresentationModelAssembler<CommandConfig, EntityModel<CommandConfig>> {

  @Override
  public @NonNull EntityModel<CommandConfig> toModel(@NonNull CommandConfig command) {
    try {
      return EntityModel.of(
          command,
          linkTo(methodOn(CommandsRestController.class).command(command.getName())).withSelfRel(),
          linkTo(methodOn(CommandsRestController.class).commands()).withRel("commands"),
          linkTo(methodOn(CommandsRestController.class).delete(command.getName()))
              .withRel("delete"));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    //
  }
}
