package no.jonasandersen.admin.adapter.in.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

/**
 * Base class for all commands. Commands should be named [CommandName]Command. The Command suffix is
 * removed when registering the command.
 */
public abstract class BaseCommand {

  protected final CommandDataImpl commandData;

  private String commandName;

  public BaseCommand(String description) {
    commandName = this.getClass().getSimpleName().toLowerCase();
    commandName = commandName.replace("command", "");
    commandData = new CommandDataImpl(commandName, description);
  }

  public void addOption(Command.Option option) {
    commandData.addOption(
        option.getType(),
        option.getName(),
        option.getDescription(),
        option.isRequired(),
        option.isAutoComplete());
  }

  public CommandDataImpl getCommandData() {
    return commandData;
  }

  public String getCommandName() {
    return commandName;
  }

  public abstract void onSlashCommand(SlashCommandInteractionEvent event);
}
