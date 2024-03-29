package no.jonasandersen.admin.adapter.in.web.shortcut;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
class ShortcutControllerTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void modelHaveShortcutsAttribute() throws Exception {
    this.mockMvc
        .perform(get("/shortcut"))
        .andExpect(status().isOk())
        .andExpect(view().name("shortcut-overview"))
        .andExpect(model().attributeExists("projectNames"));
  }
}