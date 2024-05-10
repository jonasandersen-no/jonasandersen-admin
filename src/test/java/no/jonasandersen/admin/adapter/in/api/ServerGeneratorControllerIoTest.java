package no.jonasandersen.admin.adapter.in.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
class ServerGeneratorControllerIoTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Disabled("Disabled because I don't have a null version of LinodeServerApi in test context yet.")
  void name() throws Exception {
    mockMvc.perform(post("/api/server-generator").with(csrf()))
        .andExpect(status().isOk());

  }
}