package no.jonasandersen.admin.adapter.in.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
class ServerGeneratorControllerIoTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void name() throws Exception {
    mockMvc.perform(post("/api/server-generator").with(csrf())
            .content("{\"serverType\":\"MINECRAFT\"}")
            .contentType("application/json"))
        .andExpect(status().isOk());

  }
}