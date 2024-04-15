package no.jonasandersen.admin.adapter.in.web;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import no.jonasandersen.admin.adapter.out.websocket.Model;
import no.jonasandersen.admin.core.LinodeVolumeService;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.minecraft.MinecraftService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hx/linode")
public class LinodeHxController {

  private final LinodeVolumeService linodeVolumeService;
  private final JteHtmlGenerator jteHtmlGenerator;
  private final MinecraftService minecraftService;


  public LinodeHxController(LinodeVolumeService linodeVolumeService,
      JteHtmlGenerator jteHtmlGenerator, MinecraftService minecraftService) {
    this.linodeVolumeService = linodeVolumeService;
    this.jteHtmlGenerator = jteHtmlGenerator;
    this.minecraftService = minecraftService;

  }

  @PostMapping("/instance/create")
  @HxRequest
  String createInstance() {
    Random random = new Random();
    return jteHtmlGenerator.generateHtml("linode/instance", Model.from(
        new LinodeInstance(new LinodeId(random.nextLong()), List.of("127.0.0.1"), "running",
            UUID.randomUUID().toString(),
            List.of("tags"), List.of())));
  }

  @GetMapping("/instance/refresh")
  @HxRequest
  String refreshInstance() {

    List<LinodeInstance> instances = minecraftService.getInstances();
    return instances.stream()
        .map(instance -> jteHtmlGenerator.generateHtml("linode/instance", Model.from(instance)))
        .collect(Collectors.joining());
  }

  @GetMapping("/volumes")
  @HxRequest
  String getVolumes() {
//    List<LinodeVolume> volumes = service.getVolumes();
    List<LinodeVolume> volumes = List.of();

    String collect = volumes.stream().map(linodeVolume -> STR."""
        <li>\{linodeVolume.toString()}, </li>
        """)
        .collect(Collectors.joining());

    return STR."""
        <ul>
          \{collect}
        </ul >
        """;
  }
}
