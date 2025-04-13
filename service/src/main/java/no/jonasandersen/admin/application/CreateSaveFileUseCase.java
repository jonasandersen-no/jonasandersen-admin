package no.jonasandersen.admin.application;

import java.util.UUID;
import no.jonasandersen.admin.domain.SaveFile;
import no.jonasandersen.admin.domain.SaveFileEvent;
import no.jonasandersen.admin.domain.SaveFileId;
import org.springframework.stereotype.Component;

@Component
public class CreateSaveFileUseCase {

  private final EventStore<SaveFileId, SaveFileEvent, SaveFile> eventStore;

  public CreateSaveFileUseCase(EventStore<SaveFileId, SaveFileEvent, SaveFile> eventStore) {
    this.eventStore = eventStore;
  }

  public SaveFileId createSaveFile(String name, String owner) {
    SaveFileId id = new SaveFileId(UUID.randomUUID());

    SaveFile saveFile = SaveFile.create(id, name, owner);
    eventStore.save(saveFile);

    return id;
  }
}
