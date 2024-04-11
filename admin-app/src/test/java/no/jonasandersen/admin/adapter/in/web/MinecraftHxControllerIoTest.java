package no.jonasandersen.admin.adapter.in.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

class MinecraftHxControllerIoTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Nested
  class Get {

    @Test
    @WithMockUser
    void canHitMinecraftEndpointWithHtmx() throws Exception {
      mockMvc.perform(get("/minecraft").header("HX-Request", "GET"))
          .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void normalControllersCantFindMinecraftEndpoint() throws Exception {
      mockMvc.perform(get("/minecraft"))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  class Post {

    @Test
    @WithMockUser
    void canPostToMinecraftEndpointWithHtmx() throws Exception {
      mockMvc.perform(post("/minecraft")
              .with(csrf())
              .header("HX-Request", "POST"))
          .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void postHtmlxReturnsHxTriggerHeader() throws Exception {
      mockMvc.perform(post("/minecraft")
              .with(csrf())
              .header("HX-Request", "POST"))
          .andExpect(status().isOk())
          .andExpect(header().string("HX-Trigger", "my-event"));
    }

    @Test
    @WithMockUser
    void normalControllersCantPostToMinecraftEndpoint() throws Exception {
      mockMvc.perform(post("/minecraft").with(csrf()))
          .andExpect(status().isNotFound());
    }

  }
}

