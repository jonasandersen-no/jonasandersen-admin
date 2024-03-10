package no.jonasandersen.admin.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class MinecraftControllerIoTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Nested
  class Get {

    @Test
    void canHitMinecraftEndpointWithHtmx() throws Exception {
      mockMvc.perform(get("/minecraft").header("HX-Request", "GET"))
          .andExpect(status().isOk());
    }

    @Test
    void normalControllersCantFindMinecraftEndpoint() throws Exception {
      mockMvc.perform(get("/minecraft"))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  class Post {

    @Test
    void canPostToMinecraftEndpointWithHtmx() throws Exception {
      mockMvc.perform(post("/minecraft").header("HX-Request", "POST"))
          .andExpect(status().isOk());
    }

    @Test
    void normalControllersCantPostToMinecraftEndpoint() throws Exception {
      mockMvc.perform(post("/minecraft"))
          .andExpect(status().isNotFound());
    }

  }
}

