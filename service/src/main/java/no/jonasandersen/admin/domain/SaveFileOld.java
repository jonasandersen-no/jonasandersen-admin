package no.jonasandersen.admin.domain;

public class SaveFileOld {

  private String name;
  private User owner;

  public SaveFileOld(String name) {
    this.name = name;
  }

  public SaveFileOld(SaveFileSnapshot snapshot) {
    this.name = snapshot.name();
    this.owner = snapshot.owner();
  }

  public SaveFileOld(String name, User owner) {
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
    if (!(o instanceof SaveFileOld saveFileOld)) {
      return false;
    }

    return name.equals(saveFileOld.name) && owner.equals(saveFileOld.owner);
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
