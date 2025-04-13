package no.jonasandersen.admin.domain;

import java.util.List;

public class SaveFile extends EventSourcedAggregate<SaveFileEvent, SaveFileId> {

  private String name;
  private String owner;

  private SaveFile() {}

  private SaveFile(List<SaveFileEvent> events) {
    events.forEach(this::apply);
  }

  public static SaveFile create(SaveFileId id, String name, String owner) {
    SaveFile saveFile = new SaveFile();
    saveFile.enqueue(new SaveFileCreatedEvent(id, name, owner));
    return saveFile;
  }

  @Override
  protected void apply(SaveFileEvent event) {
    switch (event) {
      case SaveFileCreatedEvent(SaveFileId id, String name, String owner) -> {
        setId(id);
        this.name = name;
        this.owner = owner;
      }
    }
  }

  public static SaveFile reconstitute(List<SaveFileEvent> events) {
    return new SaveFile(events);
  }

  public String name() {
    return name;
  }

  public String owner() {
    return owner;
  }
}
