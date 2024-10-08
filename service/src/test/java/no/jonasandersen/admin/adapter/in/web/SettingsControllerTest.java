package no.jonasandersen.admin.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.jonasandersen.admin.adapter.out.user.CrudPermittedUserRepository;
import no.jonasandersen.admin.adapter.out.user.PermittedUserDbo;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
class SettingsControllerTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CrudPermittedUserRepository repository;

  @Test
  @WithMockUser(roles = "ADMIN")
  void addUserToAccessControl() throws Exception {

    mockMvc.perform(post("/settings/allow-user")
            .with(csrf())
            .param("email", "email@example.com"))
        .andExpect(status().is3xxRedirection());

    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void nonAdminCanNotAllowUser() throws Exception {
    mockMvc.perform(post("/settings/allow-user")
            .with(csrf())
            .param("email", "email@example.com"))
        .andExpect(status().isForbidden());
  }

  @Test
  void allowedUserShownInModel() throws Exception {
    PermittedUserDbo entity = new PermittedUserDbo();
    entity.setEmail("email@example.com");
    repository.save(entity);

    mockMvc.perform(get("/settings"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("allowedUsers", hasItem(User.createUser("email@example.com"))));
  }

  @Test
  void nonAdminCanNotRevokeAccess() throws Exception {
    mockMvc.perform(delete("/settings/revoke-user")
            .with(csrf())
            .param("email", "email@example.com"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void revokeAccessOfAllowedUser() throws Exception {
    PermittedUserDbo entity = new PermittedUserDbo();
    entity.setEmail("email@example.com");
    repository.save(entity);

    mockMvc.perform(delete("/settings/revoke-user")
        .with(csrf())
        .param("email", "email@example.com"))
        .andExpect(status().is3xxRedirection());

    mockMvc.perform(get("/settings"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("allowedUsers", hasSize(0)));
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }
}