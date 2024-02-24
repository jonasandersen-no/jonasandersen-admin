package no.jonasandersen.admin.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcExtensions.hxGet;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class MinecraftControllerTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void canHitMinecraftEndpointWithHtmx() throws Exception {
    mockMvc.perform(hxGet("/minecraft"))
        .andExpect(status().isOk())
        .andExpect(content().string("""
            <p> Name: Test </p>
            <p> IP: 127.0.0.1 </p>
            """));
  }

  @Test
  void normalControllersCantFindMinecraftEndpoint() throws Exception {
    mockMvc.perform(get("/minecraft"))
        .andExpect(status().isNotFound());
  }
}