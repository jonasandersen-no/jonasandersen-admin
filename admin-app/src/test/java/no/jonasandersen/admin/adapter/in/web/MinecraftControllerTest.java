package no.jonasandersen.admin.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MinecraftControllerTest {

  @Test
  void getMinecraftReturnsValidHtml() {
    MinecraftController controller = new MinecraftController(new MinecraftHtmlFormatter());

    String result = controller.getMinecraft();

    assertThat(result).isEqualTo(
        """
            <p> Name: Test </p>
            <p> IP: 127.0.0.1 </p>
                """);
  }
}