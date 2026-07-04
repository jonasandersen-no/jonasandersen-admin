
package no.jonasandersen.admin.commands;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CommandAssembler
    implements RepresentationModelAssembler<Command, EntityModel<Command>> {

  @Override
  public @NonNull EntityModel<Command> toModel(@NonNull Command command) {
    try {
      return EntityModel.of(
          command,
          linkTo(methodOn(CommandsRestController.class).command(command.getName())).withSelfRel(),
          linkTo(methodOn(CommandsRestController.class).commands())
              .withRel("commands"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
//        linkTo(methodOn(CommandsRestController.class).deleteMerchant(command.getId())).withRel("delete"));
  }
}