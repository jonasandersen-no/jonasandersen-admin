package no.jonasandersen.admin;

import java.util.function.Predicate;
import org.springframework.stereotype.Component;

@Component
public class UseStubPredicate implements Predicate<String> {

  private final AdminProperties properties;

  public UseStubPredicate(AdminProperties properties) {
    this.properties = properties;
  }

  @Override
  public boolean test(String className) {
    if (className == null) {
      return true;
    }
    return properties.stub().getOrDefault(className.toLowerCase(), true);
  }
}
