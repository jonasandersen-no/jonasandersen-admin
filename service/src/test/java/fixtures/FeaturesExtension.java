package fixtures;

import no.jonasandersen.admin.infrastructure.Features;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class FeaturesExtension implements BeforeEachCallback {

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    Features.reset();
  }
}
