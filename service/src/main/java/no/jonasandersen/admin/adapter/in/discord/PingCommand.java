package no.jonasandersen.admin.adapter.in.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class PingCommand extends BaseCommand {

  public PingCommand() {
    super("Pings the server");
  }

  @Override
  public void onSlashCommand(SlashCommandInteractionEvent event) {
    event.reply("Pong!").setEphemeral(true).queue();
  }
}
