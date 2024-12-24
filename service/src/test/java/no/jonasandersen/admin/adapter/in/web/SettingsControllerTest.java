package no.jonasandersen.admin.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;
import no.jonasandersen.admin.adapter.out.user.CrudPermittedUserRepository;
import no.jonasandersen.admin.adapter.out.user.PermittedUserDbo;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WithMockUser
class SettingsControllerTest extends IoBasedTest {

  @Autowired
  private MockMvcTester mockMvc;

  @Autowired
  private CrudPermittedUserRepository repository;

  @Test
  @WithMockUser(roles = "ADMIN")
  void addUserToAccessControl() {
    mockMvc.post().uri("/settings/allow-user")
        .with(csrf())
        .param("email", "email@example.com")
        .assertThat()
        .hasStatus3xxRedirection();

    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void nonAdminCanNotAllowUser() {
    mockMvc.post().uri("/settings/allow-user")
        .with(csrf())
        .param("email", "email@example.com")
        .assertThat()
        .hasStatus(HttpStatus.FORBIDDEN);
  }

  @Test
  void allowedUserShownInModel() {
    PermittedUserDbo entity = new PermittedUserDbo();
    entity.setEmail("email@example.com");
    repository.save(entity);

    mockMvc.get().uri("/settings")
        .assertThat()
        .hasStatusOk()
        .model().extracting("allowedUsers")
        .isEqualTo(List.of(User.createUser("email@example.com")));
  }

  @Test
  void nonAdminCanNotRevokeAccess() {
    mockMvc.delete().uri("/settings/revoke-user")
        .with(csrf())
        .param("email", "email@example.com")
        .assertThat()
        .hasStatus(HttpStatus.FORBIDDEN);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void revokeAccessOfAllowedUser() throws Exception {
    PermittedUserDbo entity = new PermittedUserDbo();
    entity.setEmail("email@example.com");
    repository.save(entity);

    mockMvc.delete().uri("/settings/revoke-user")
        .with(csrf())
        .param("email", "email@example.com")
        .assertThat()
        .hasStatus3xxRedirection();

    mockMvc.get().uri("/settings")
        .assertThat()
        .hasStatusOk()
        .model().hasFieldOrPropertyWithValue("allowedUsers", List.of());
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }
}