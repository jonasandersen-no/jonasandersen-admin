package no.jonasandersen.admin.adapter.in.discord;

import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import no.jonasandersen.admin.adapter.out.savefile.SaveFileProjection;
import no.jonasandersen.admin.application.CreateSaveFileUseCase;
import no.jonasandersen.admin.domain.SaveFilesForOwner;
import org.springframework.stereotype.Component;

//@Component
public class WorldCommand extends BaseCommand {

  private final CreateSaveFileUseCase createSaveFileUseCase;
  private final SaveFileProjection saveFileProjection;

  public WorldCommand(
      CreateSaveFileUseCase createSaveFileUseCase, SaveFileProjection saveFileProjection) {
    super("Used for creating,starting,stopping minecraft worlds");
    this.createSaveFileUseCase = createSaveFileUseCase;
    this.saveFileProjection = saveFileProjection;
    addOptions(
        new OptionData(OptionType.STRING, "command", "What do you want to do?")
            .addChoice("Create", "create")
            .addChoice("List", "list"));
  }

  @Override
  public void onSlashCommand(SlashCommandInteractionEvent event) {
    OptionMapping command = event.getOption("command");
    if (command == null) {
      event.reply("Error: Missing command. Check the required options for this command").queue();
    }
    switch (command.getAsString()) {
      case "create" -> {
        createSaveFileUseCase.createSaveFile("save-1", event.getUser().getId());
        event.reply("Created world with name save-1").queue();
      }
      case "list" -> {
        SaveFilesForOwner saveFilesForOwner =
            saveFileProjection.saveFilesForOwner(event.getUser().getId());

        if (saveFilesForOwner == null) {
          event.reply("No worlds found for user " + event.getUser().getGlobalName()).queue();
        }

        List<String> saveFiles = saveFilesForOwner.saveFilesNames();

        String list = saveFiles.stream().map("- %s"::formatted).collect(Collectors.joining("\n"));

        event
            .reply(
                """
        World names:
        %s
        """
                    .formatted(list))
            .queue();
      }
      default -> {}
    }
  }
}
