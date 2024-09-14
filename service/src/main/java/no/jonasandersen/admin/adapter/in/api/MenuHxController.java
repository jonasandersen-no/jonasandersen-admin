package no.jonasandersen.admin.adapter.in.api;

import java.time.LocalDate;
import java.util.List;
import no.jonasandersen.admin.application.MenuService;
import no.jonasandersen.admin.domain.MenuItem;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuHxController {

  public static final String MENU_ITEM_HTML = """
      <div class="input-group mb-3">
        <span class="input-group-text">%s</span>
      <input type="text" class="form-control" value="%s" name='%s'/>
      </div>
      """;
  private final MenuService menuService;

  public MenuHxController(MenuService menuService) {
    this.menuService = menuService;
  }

  @GetMapping(value = "/api/hx/menu/edit", headers = "Hx-Request")
  String editMenu() {
    List<String> descriptions = menuService.listMenu().stream()
        .map(i -> MENU_ITEM_HTML.formatted(i.formattedDate(), i.description(), i.date())
        )
        .toList();

    return """
        <div class='d-inline-flex gap-4'>
        <h1>Edit menu</h1>
        <button class="btn btn-secondary" hx-get="/api/hx/menu/new-line" hx-target="#menu-items" hx-swap="beforeend" >Add</button>
        </div>
        <form hx-post="/api/hx/menu/edit" hx-target="#menu">
        <div id='menu-items'>
        %s
        </div>
        <button class="btn btn-primary" >Save</button>
        </form>
        
        """.formatted(String.join(System.lineSeparator(), descriptions));
  }

  int counter = 2;

  @GetMapping(value = "/api/hx/menu/new-line", headers = "Hx-Request")
  String newLine() {
    var i = new MenuItem(LocalDate.now().plusDays(counter++), "Laks");
    return MENU_ITEM_HTML.formatted(i.formattedDate(), i.description(), i.date());

  }

  @PostMapping(value = "/api/hx/menu/edit", headers = "Hx-Request", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  String saveMenu(@RequestBody MultiValueMap<String, String> data) {

    menuService.update(data);

    String htmlItem = """
        <div>
          <h3>%s</h3>
          <p>%s</p>
        </div>
        """;
    List<String> html = menuService.listMenu().stream()
        .map(item -> htmlItem.formatted(item.formattedDate(), item.description()))
        .toList();

    String button = """
        <button class="btn btn-primary" hx-get="/api/hx/menu/edit" hx-target="#menu">Edit</button>
        """;

    return button + String.join("", html);
  }
}
