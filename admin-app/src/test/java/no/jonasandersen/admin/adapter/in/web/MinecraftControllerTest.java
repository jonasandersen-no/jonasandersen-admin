package no.jonasandersen.admin.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.EmptyValidator;
import no.jonasandersen.admin.core.minecraft.MinecraftService;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.core.minecraft.port.TestServerApi;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MinecraftControllerTest {

  @Nested
  class Get {

    @Test
    void returnsHtmlWithDataWhenInstanceIsPresent() {
      ServerApi api = () -> new MinecraftInstance("minecraft-auto-config-1", "127.0.0.1",
          "running");

      MinecraftController controller = new MinecraftController(
          new MinecraftHtmlFormatter(new EmptyValidator()),
          new MinecraftService(api));

      String result = controller.getMinecraft();

      assertThat(result).isEqualTo(
          """
              <p> Name: minecraft-auto-config-1 </p>
              <p> IP: 127.0.0.1 </p>
              <p> Status: running </p>
                  """);
    }

    @Test
    void returnsNoServerHtmlWhenInstanceIsNotPresent() {
      ServerApi api = MinecraftInstance::new;

      MinecraftController controller = new MinecraftController(
          new MinecraftHtmlFormatter(new EmptyValidator()),
          new MinecraftService(api));

      String result = controller.getMinecraft();

      assertThat(result).isEqualTo("""
          <p> No server is running </p>
          """);
    }
  }


  @Test
  void postMinecraftReturnsValidHtml() {
    MinecraftController controller = new MinecraftController(
        new MinecraftHtmlFormatter(new EmptyValidator()),
        new MinecraftService(new TestServerApi()));

    String result = controller.postMinecraft();

    assertThat(result).isEqualTo("Start server");
  }
}