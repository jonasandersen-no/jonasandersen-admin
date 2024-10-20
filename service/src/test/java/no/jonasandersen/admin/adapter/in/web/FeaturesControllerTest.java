package no.jonasandersen.admin.adapter.in.web;

import static org.junit.jupiter.api.Assertions.*;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
class FeaturesControllerTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void featuresEndpointFound() throws Exception {
    mockMvc.perform(get("/features"))
        .andExpect(status().isOk());

  }
}