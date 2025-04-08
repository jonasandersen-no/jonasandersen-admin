package no.jonasandersen.admin.domain;

public class SaveFile {

  private String name;
  private User owner;

  public SaveFile(String name) {
    this.name = name;
  }

  public SaveFile(SaveFileSnapshot snapshot) {
    this.name = snapshot.name();
    this.owner = snapshot.owner();
  }

  public SaveFile(String name, User owner) {
    this.name = name;
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public User getOwner() {
    return owner;
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof SaveFile saveFile)) {
      return false;
    }

    return name.equals(saveFile.name) && owner.equals(saveFile.owner);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + owner.hashCode();
    return result;
  }

  public SaveFileSnapshot snapshot() {
    return new SaveFileSnapshot(name, owner);
  }

  public record SaveFileSnapshot(String name, User owner) {}
}
