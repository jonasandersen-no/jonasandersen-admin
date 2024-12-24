package no.jonasandersen.admin.adapter.in.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import no.jonasandersen.admin.application.ServerGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

class ServerGeneratorControllerIoTest {

  private final MockMvcTester mockMvc = MockMvcTester.of(
      new ServerGeneratorController(ServerGenerator.createNull()));

  @Test
  void minecraftServerTypeReturns200() throws Exception {
    mockMvc.post().uri("/api/server-generator").with(csrf())
        .content("""
            {
            "serverType": "MINECRAFT"
            }""")
        .contentType("application/json")
        .assertThat()
        .hasStatusOk();
  }

  @Test
  void defaultServerTypeThrows500() throws Exception {
    mockMvc.post().uri("/api/server-generator").with(csrf())
        .content("""
            {
            "serverType": "DEFAULT"
            }""")
        .contentType("application/json")
        .assertThat()
        .hasStatus(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}