package no.jonasandersen.admin.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.EmptyValidator;
import no.jonasandersen.admin.core.minecraft.MinecraftService;
import no.jonasandersen.admin.core.minecraft.port.TestServerApi;
import org.junit.jupiter.api.Test;

class MinecraftControllerTest {

  @Test
  void getMinecraftReturnsValidHtml() {
    MinecraftController controller = new MinecraftController(
        new MinecraftHtmlFormatter(new EmptyValidator()),
        new MinecraftService(new TestServerApi()));

    String result = controller.getMinecraft();

    assertThat(result).isEqualTo(
        """
            <p> Name: minecraft-auto-config-1 </p>
            <p> IP: 127.0.0.1 </p>
            <p> Status: running </p>
                """);
  }

  @Test
  void postMinecraftReturnsValidHtml() {
    MinecraftController controller = new MinecraftController(
        new MinecraftHtmlFormatter(new EmptyValidator()),
        new MinecraftService(new TestServerApi()));

    String result = controller.postMinecraft();

    assertThat(result).isEqualTo("Stop server");
  }
}