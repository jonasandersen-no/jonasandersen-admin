package no.jonasandersen.admin.adapter.in.web;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WithMockUser
class DnsControllerTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getIndex() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/dns"))
        .andExpect(status().isOk())
        .andExpect(view().name("dns/index"));
  }

  @Test
  void modelWithDnsRecords() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/dns"))
        .andExpect(status().isOk())
        .andExpect(view().name("dns/index"))
        .andExpect(model().attribute("dnsRecords", notNullValue()));
  }
}