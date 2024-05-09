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
class LinodeApiControllerIoTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void name() throws Exception {
    mockMvc.perform(post("/api/linode").with(csrf()))
        .andExpect(status().isOk());

  }
}