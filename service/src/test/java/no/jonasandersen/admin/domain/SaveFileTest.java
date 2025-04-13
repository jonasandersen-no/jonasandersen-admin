package no.jonasandersen.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.application.port.UUIDGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SaveFileTest {

  @Nested
  class CommandsGenerateEvents {

    @Test
    void createGeneratesSaveFileEventWithId() {

      SaveFileId id = new SaveFileId(UUIDGenerator.generate());

      SaveFile saveFile = SaveFile.create(id, "mySaveFile");

      List<SaveFileEvent> events = saveFile.uncommittedEvents();

      assertThat(events).containsExactly(new SaveFileCreatedEvent(id, "mySaveFile"));
    }
  }

  @Nested
  class EventsGenerateState {

    @Test
    void saveFileCreatedUpdatesSaveFileWithName() {
      SaveFileId id = new SaveFileId(UUIDGenerator.generate());
      String name = "mySaveFile";

      List<SaveFileEvent> events = List.of(new SaveFileCreatedEvent(id, name));

      SaveFile saveFile = SaveFile.reconstitute(events);

      assertThat(saveFile.getId()).isEqualTo(id);
      assertThat(saveFile.name()).isEqualTo(name);
    }
  }
}
