package no.jonasandersen.admin.adapter.in.web;

import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.adapter.in.web.layout.MainLayoutViewComponent;
import no.jonasandersen.admin.adapter.in.web.linode.LinodeDetailViewComponent;
import no.jonasandersen.admin.adapter.in.web.linode.LinodeViewComponent;
import no.jonasandersen.admin.core.domain.LinodeId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/linode")
public class LinodeController {

  private final MainLayoutViewComponent mainLayoutViewComponent;
  private final LinodeViewComponent linodeViewComponent;
  private final LinodeDetailViewComponent linodeDetailViewComponent;

  public LinodeController(MainLayoutViewComponent mainLayoutViewComponent,
      LinodeViewComponent linodeViewComponent,
      LinodeDetailViewComponent linodeDetailViewComponent) {
    this.mainLayoutViewComponent = mainLayoutViewComponent;
    this.linodeViewComponent = linodeViewComponent;
    this.linodeDetailViewComponent = linodeDetailViewComponent;
  }

  @GetMapping
  ViewContext linode() {
    return mainLayoutViewComponent.render("Linode", linodeViewComponent.render());
  }

  @GetMapping("/{linodeId}")
  ViewContext getInstance(@PathVariable Long linodeId) {
    return mainLayoutViewComponent.render("Linode Detail - " + linodeId,
        linodeDetailViewComponent.render(new LinodeId(linodeId)));
  }

}
