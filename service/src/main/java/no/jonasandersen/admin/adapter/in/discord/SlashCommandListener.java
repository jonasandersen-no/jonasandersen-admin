package no.jonasandersen.admin.adapter.in.discord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SlashCommandListener extends ListenerAdapter {

  private final Logger logger = LoggerFactory.getLogger(SlashCommandListener.class);
  private final Map<String, BaseCommand> commandMap;

  public SlashCommandListener(List<? extends BaseCommand> commands) {
    this.commandMap = new HashMap<>();
    for (BaseCommand command : commands) {
      commandMap.put(command.getCommandName(), command);
    }
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

    event.getName();

    BaseCommand command = commandMap.get(event.getName());

    if (command != null) {
      command.onSlashCommand(event);
      return;
    }

    logger.error("Failed to execute command {}", event.getName());
    event.getHook().sendMessage("Failed to execute command").queue();
  }

  @Override
  public void onReady(ReadyEvent event) {
    SelfUser selfUser = event.getJDA().getSelfUser();
    logger.info("Logged in as {}#{}", selfUser.getName(), selfUser.getDiscriminator());

    JDA jda = event.getJDA();

    CommandListUpdateAction updateAction = jda.updateCommands();

    List<Command> existingCommands = new ArrayList<>();
    jda.retrieveCommands()
        .queue(
            commands -> {
              for (Command command : commands) {
                if (!commandMap.containsKey(command.getName())) {
                  logger.info(
                      "Command /{} is no longer provided by this application. Removing command",
                      command.getName());
                  jda.deleteCommandById(command.getId()).queue();
                }
              }
              List<CommandData> commandData =
                  commandMap.values().stream()
                      .map(BaseCommand::getCommandData)
                      .collect(Collectors.toList());

              logger.debug(
                  "Registering commands: {}",
                  commandData.stream().map(CommandData::getName).collect(Collectors.joining(", ")));

              updateAction.addCommands(commandData).queue();
            });
  }
}
