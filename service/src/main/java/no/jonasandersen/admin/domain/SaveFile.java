package no.jonasandersen.admin.domain;

import java.util.List;

public class SaveFile extends EventSourcedAggregate<SaveFileEvent, SaveFileId> {

  private String name;

  private SaveFile() {}

  private SaveFile(List<SaveFileEvent> events) {
    events.forEach(this::apply);
  }

  public static SaveFile create(SaveFileId id, String name) {
    SaveFile saveFile = new SaveFile();
    saveFile.enqueue(new SaveFileCreatedEvent(id, name));
    return saveFile;
  }

  @Override
  protected void apply(SaveFileEvent event) {
    switch (event) {
      case SaveFileCreatedEvent(SaveFileId id, String name) -> {
        setId(id);
        this.name = name;
      }
    }
  }

  public static SaveFile reconstitute(List<SaveFileEvent> events) {
    return new SaveFile(events);
  }

  public String name() {
    return name;
  }
}
