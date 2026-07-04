package no.jonasandersen.admin.github;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import no.jonasandersen.admin.ModuleTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ModuleTest
class CommandLoaderTest {

  @Autowired GitConfigService gitConfigService;

  @Autowired CommandLoader commandLoader;

  @Test
  void test() throws IOException {

    CommandConfig test = commandLoader.load("test");

    System.out.println(test);
  }
}
