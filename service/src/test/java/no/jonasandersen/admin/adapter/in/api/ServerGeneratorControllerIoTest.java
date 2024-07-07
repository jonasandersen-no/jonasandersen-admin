package no.jonasandersen.admin.adapter.in.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.config.WebBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ServerGeneratorController.class)
class ServerGeneratorControllerIoTest extends WebBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void minecraftServerTypeReturns200() throws Exception {
    mockMvc.perform(post("/api/server-generator").with(csrf())
            .content("""
                {
                "serverType": "MINECRAFT"
                }""")
            .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void defaultServerTypeThrows500() throws Exception {
    mockMvc.perform(post("/api/server-generator").with(csrf())
            .content("""
                {
                "serverType": "DEFAULT"
                }""")
            .contentType("application/json"))
        .andExpect(status().isInternalServerError());
  }

//  @Test
//  void responseBodyContainsUsername() throws Exception {
//    assertThat(mockMvc.perform(post("/api/server-generator").with(csrf())
//        .content("""
//            {
//            "serverType": "MINECRAFT"
//            }""")
//        .contentType("application/json")))
//        .matches(status().isOk());
//  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    ServerGenerator serverGenerator() {
      return ServerGenerator.createNull();
    }
  }
}