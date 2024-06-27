package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.adapter.UsernameResolver;
import no.jonasandersen.admin.application.LinodeService;
import no.jonasandersen.admin.application.LinodeVolumeService;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.domain.InstanceCreatedEvent;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.domain.Subdomain;
import no.jonasandersen.admin.domain.VolumeId;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/linode")
public class LinodeController {

  private static final Logger log = LoggerFactory.getLogger(LinodeController.class);
  private final AdminProperties properties;
  private final ServerGenerator serverGenerator;
  private final LinodeVolumeService volumeService;
  private final LinodeService service;
  private final ApplicationEventPublisher events;

  public LinodeController(AdminProperties properties, ServerGenerator serverGenerator,
      LinodeVolumeService volumeService, LinodeService service, ApplicationEventPublisher events) {
    this.properties = properties;
    this.serverGenerator = serverGenerator;
    this.volumeService = volumeService;
    this.service = service;
    this.events = events;
  }

  @GetMapping()
  String linode(Model model) {
    model.addAttribute("instances", service.getInstances());
    return "linode/index";
  }

  @GetMapping("/{linodeId}")
  String getInstance(@PathVariable Long linodeId, Model model, RedirectAttributes redirectAttrs) {
    Optional<LinodeInstance> instance;
    try {
      instance = service.findInstanceById(new LinodeId(linodeId));
    } catch (HttpClientErrorException e) {
      log.error("Failed to get instance with id {}", linodeId);
      log.error(e.getMessage());
      redirectAttrs.addFlashAttribute("message", "Failed to get instance with id " + linodeId);
      return "redirect:/linode";
    }

    if (instance.isEmpty()) {
      return "redirect:/linode";
    }

    model.addAttribute("instance", instance.get());
    return "linode/linode-detail";
  }

  @PostMapping("/{linodeId}")
  @Transactional
  String installMinecraft(@PathVariable Long linodeId, RedirectAttributes redirectAttrs) {
    LinodeInstance instance = service.findInstanceById(LinodeId.from(linodeId)).orElseThrow();

    if (!instance.status().equals("running")) {
      redirectAttrs.addFlashAttribute("message", "Instance is not running");
      return "redirect:/linode";
    }

    Long volumeId = properties.linode().volumeId();

    events.publishEvent(
        new InstanceCreatedEvent(LinodeId.from(linodeId), VolumeId.from(volumeId), ServerType.MINECRAFT));

    redirectAttrs.addFlashAttribute("message", "Minecraft server is being installed");
    return "redirect:/linode";
  }

  @GetMapping("/create")
  String create(Model model) {
    model.addAttribute("serverTypes", List.of(ServerType.values()));

    return "linode/create";
  }

  @PostMapping("/create")
  String createResponse(@RequestParam ServerType serverType, @RequestParam String subdomain) {
    log.info("Creating server of type {} with subdomain '{}'", serverType, subdomain);

    if (subdomain == null || subdomain.isBlank()) {
      log.info("Creating server of type {} with auto generated subdomain", serverType);
      serverGenerator.generate(UsernameResolver.getUsername(), serverType);
    } else {
      serverGenerator.generate(UsernameResolver.getUsername(), serverType, Subdomain.of(subdomain));
    }
    return "redirect:/linode";
  }

}
