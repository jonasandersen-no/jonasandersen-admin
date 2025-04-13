package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.domain.SaveFile;
import no.jonasandersen.admin.domain.SaveFileCreatedEvent;
import no.jonasandersen.admin.domain.SaveFileEvent;
import no.jonasandersen.admin.domain.SaveFileId;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class CreateSaveFileUseCaseTest {

  @Test
  void createSaveFileCreatesSaveFile() {
    Fixture fixture = useCaseWithEventStore();

    SaveFileId id = fixture.useCase().createSaveFile("saveFile1", "jonas");

    assertThat(id).isNotNull();

    assertThat(fixture.eventStore().allEvents(id))
        .containsExactly(new SaveFileCreatedEvent(id, "saveFile1", "jonas"));
  }

  private record Fixture(
      EventStore<SaveFileId, SaveFileEvent, SaveFile> eventStore, CreateSaveFileUseCase useCase) {}

  private static @NotNull Fixture useCaseWithEventStore() {
    EventStore<SaveFileId, SaveFileEvent, SaveFile> eventStore = EventStore.forSaveFiles();
    CreateSaveFileUseCase useCase = new CreateSaveFileUseCase(eventStore);
    return new Fixture(eventStore, useCase);
  }
}
