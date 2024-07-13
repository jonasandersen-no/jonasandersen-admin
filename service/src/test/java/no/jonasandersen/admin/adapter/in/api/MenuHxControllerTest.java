package no.jonasandersen.admin.adapter.in.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.application.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class MenuHxControllerTest {

  @Test
  void formDataConvertedToMenuItem() {
    MenuHxController controller = new MenuHxController(MenuService.configureForTest());

    MultiValueMap<String, String> data = new LinkedMultiValueMap<>();

    data.put("2024-07-10", List.of("Pasta"));

    String html = controller.saveMenu(data);

    assertThat(html).isEqualTo("""
        <button class="btn btn-primary" hx-get="/api/hx/menu/edit" hx-target="#menu">Edit</button>
        <div>
          <h3>Wednesday 10. July</h3>
          <p>Pasta</p>
        </div>
        """);
  }
}