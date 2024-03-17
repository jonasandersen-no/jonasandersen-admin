package no.jonasandersen.admin.adapter.in.web.shortcut;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
class ShortcutHxControllerTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ShortcutRepository shortcutRepository;

  @Nested
  class Post {

    @Test
    void canAccessWithHtmx() throws Exception {
      mockMvc
          .perform(post("/hx/shortcut")
              .header("HX-Request", "POST")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .content("project=project&shortcut=shortcut&description=description")
              .with(csrf()))
          .andExpect(status().isOk());
    }

    @Test
    void canAccessWithoutHtmx() throws Exception {
      mockMvc
          .perform(post("/hx/shortcut")
              .with(csrf()))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  class Edit {

    @Test
    void canAccessWithHtmxSave() throws Exception {
      Shortcut saved = shortcutRepository.save(
          new Shortcut(null, "EDIT_HTMX", "shortcut", "description"));

      List<Shortcut> shortcuts = shortcutRepository.findAll();
      Shortcut entity = shortcuts.stream()
          .filter(shortcut -> shortcut.project().equalsIgnoreCase(saved.project())
              && shortcut.shortcut().equalsIgnoreCase(saved.shortcut())
              && shortcut.description().equalsIgnoreCase(saved.description()))
          .findFirst().orElseThrow();

      mockMvc.perform(put(STR."/hx/shortcut/edit/\{entity.id()}")
              .header("HX-Request", "PUT")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .content(STR."""
              project=\{entity.project()}\
              &shortcut=\{entity.shortcut()}\
              &description=\{entity.description()}
              """).with(csrf()))
          .andExpect(status().isOk());
    }

    @Test
    void canNotAccessWithoutHtmxSave() throws Exception {
      mockMvc
          .perform(put("/hx/shortcut/edit/1")
              .with(csrf()))
          .andExpect(status().isNotFound());
    }

    @Test
    void canAccessWithHtmxCancel() throws Exception {
      Shortcut saved = shortcutRepository.save(
          new Shortcut(null, "EDIT_HTMX_CANCEL", "shortcut", "description"));

      List<Shortcut> shortcuts = shortcutRepository.findAll();
      Shortcut entity = shortcuts.stream()
          .filter(shortcut -> shortcut.project().equalsIgnoreCase(saved.project())
              && shortcut.shortcut().equalsIgnoreCase(saved.shortcut())
              && shortcut.description().equalsIgnoreCase(saved.description()))
          .findFirst().orElseThrow();

      mockMvc.perform(get(STR."/hx/shortcut/edit/cancel/\{entity.id()}")
              .header("HX-Request", "GET")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .content(STR."""
              project=\{entity.project()}\
              &shortcut=\{entity.shortcut()}\
              &description=\{entity.description()}
              """).with(csrf()))
          .andExpect(status().isOk());
    }

    @Test
    void canNotAccessWithoutHtmxCancel() throws Exception {
      mockMvc.perform(get("/hx/shortcut/edit/cancel/1"))
          .andExpect(status().isNotFound());
    }

    @Test
    void canAccessWithHtmxEdit() throws Exception {
      Shortcut saved = shortcutRepository.save(
          new Shortcut(null, "EDIT_HTMX_EDIT", "shortcut", "description"));

      List<Shortcut> shortcuts = shortcutRepository.findAll();
      Shortcut entity = shortcuts.stream()
          .filter(shortcut -> shortcut.project().equalsIgnoreCase(saved.project())
              && shortcut.shortcut().equalsIgnoreCase(saved.shortcut())
              && shortcut.description().equalsIgnoreCase(saved.description()))
          .findFirst().orElseThrow();

      mockMvc.perform(get(STR."/hx/shortcut/edit/\{entity.id()}")
              .header("HX-Request", "GET")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .content(STR."""
              project=\{entity.project()}\
              &shortcut=\{entity.shortcut()}\
              &description=\{entity.description()}
              """).with(csrf()))
          .andExpect(status().isOk());
    }

    @Test
    void canNotAccessWithoutHtmxEdit() throws Exception {
      mockMvc.perform(get("/hx/shortcut/edit/1"))
          .andExpect(status().isNotFound());
    }
  }

}