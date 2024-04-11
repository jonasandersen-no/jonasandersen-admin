package no.jonasandersen.admin.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.EmptyValidator;
import no.jonasandersen.admin.core.LinodeVolumeService;
import no.jonasandersen.admin.core.minecraft.MinecraftService;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.core.minecraft.port.TestServerApi;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MinecraftHxControllerTest {

  @Nested
  class Get {

    @Test
    void returnsHtmlWithDataWhenInstanceIsPresent() {
      ServerApi api = new TestServerApi();

      MinecraftHxController controller = new MinecraftHxController(
          new MinecraftHtmlFormatter(new EmptyValidator()),
          new MinecraftService(api, new LinodeVolumeService(api)));

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
      ServerApi api = new TestServerApi();

      MinecraftHxController controller = new MinecraftHxController(
          new MinecraftHtmlFormatter(new EmptyValidator()),
          new MinecraftService(api, new LinodeVolumeService(api)));

      String result = controller.getMinecraft();

      assertThat(result).isEqualTo("""
          <p> No server is running </p>
          """);
    }
  }


  @Test
  void postMinecraftReturnsValidHtml() {
    MinecraftHxController controller = new MinecraftHxController(
        new MinecraftHtmlFormatter(new EmptyValidator()),
        new MinecraftService(new TestServerApi(), new LinodeVolumeService(new TestServerApi())));

    String result = controller.postMinecraft();

    assertThat(result).isEqualTo("Start server");
  }
}