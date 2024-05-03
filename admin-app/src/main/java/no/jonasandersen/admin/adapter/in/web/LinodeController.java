package no.jonasandersen.admin.adapter.in.web;

import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import java.util.List;
import no.jonasandersen.admin.adapter.in.web.layout.MainLayoutViewComponent;
import no.jonasandersen.admin.adapter.in.web.linode.LinodeDetailViewComponent;
import no.jonasandersen.admin.adapter.in.web.linode.LinodeViewComponent;
import no.jonasandersen.admin.adapter.in.web.linode.create.CreateFormComponent;
import no.jonasandersen.admin.core.LinodeVolumeService;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.core.minecraft.MinecraftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/linode")
public class LinodeController {

  private static final Logger log = LoggerFactory.getLogger(LinodeController.class);
  private final MainLayoutViewComponent mainLayoutViewComponent;
  private final LinodeViewComponent linodeViewComponent;
  private final LinodeDetailViewComponent linodeDetailViewComponent;

  private final CreateFormComponent createFormComponent;
  private final LinodeVolumeService linodeVolumeService;
  private final MinecraftService minecraftService;

  public LinodeController(MainLayoutViewComponent mainLayoutViewComponent,
      LinodeViewComponent linodeViewComponent,
      LinodeDetailViewComponent linodeDetailViewComponent,
      CreateFormComponent createFormComponent, LinodeVolumeService linodeVolumeService,
      MinecraftService minecraftService) {
    this.mainLayoutViewComponent = mainLayoutViewComponent;
    this.linodeViewComponent = linodeViewComponent;
    this.linodeDetailViewComponent = linodeDetailViewComponent;
    this.createFormComponent = createFormComponent;
    this.linodeVolumeService = linodeVolumeService;
    this.minecraftService = minecraftService;
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

  @GetMapping("/create")
  ViewContext create() {

    List<LinodeVolume> volumes = linodeVolumeService.getVolumes();
    return mainLayoutViewComponent.render("Create Linode", createFormComponent.render(volumes));
  }

  @PostMapping("/create")
  ViewContext createResponse(@RequestParam String instanceName, @RequestParam Long volumeId) {
    log.info("Creating Linode with name: {} and volumeId: {}", instanceName, volumeId);

    minecraftService.createLinode(instanceName, new VolumeId(volumeId));
    return linode();
  }

}
