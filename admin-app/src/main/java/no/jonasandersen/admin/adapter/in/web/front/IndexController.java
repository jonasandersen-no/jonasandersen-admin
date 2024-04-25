package no.jonasandersen.admin.adapter.in.web.front;

import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.adapter.in.web.layout.MainLayoutViewComponent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  private final MainLayoutViewComponent mainLayoutViewComponent;
  private final IndexViewController indexViewController;

  public IndexController(MainLayoutViewComponent mainLayoutViewComponent,
      IndexViewController indexViewController) {
    this.mainLayoutViewComponent = mainLayoutViewComponent;
    this.indexViewController = indexViewController;
  }

  @GetMapping("/")
  public ViewContext index() {
    return mainLayoutViewComponent.render("Index", indexViewController.render());
  }

}
