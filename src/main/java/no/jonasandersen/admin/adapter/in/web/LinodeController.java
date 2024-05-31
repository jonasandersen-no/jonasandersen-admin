package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import no.jonasandersen.admin.application.LinodeService;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.core.minecraft.LinodeVolumeService;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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

  public LinodeController(AdminProperties properties, ServerGenerator serverGenerator,
      LinodeVolumeService volumeService, LinodeService service) {
    this.properties = properties;
    this.serverGenerator = serverGenerator;
    this.volumeService = volumeService;
    this.service = service;
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
  String installMinecraft(@PathVariable Long linodeId, RedirectAttributes redirectAttrs) {
    LinodeInstance instance = service.findInstanceById(LinodeId.from(linodeId)).orElseThrow();

    if (!instance.status().equals("running")) {
      redirectAttrs.addFlashAttribute("message", "Instance is not running");
      return "redirect:/linode";
    }

    String password = properties.minecraft().password();
    Long volumeId = properties.linode().volumeId();
    CompletableFuture.supplyAsync(() -> {
      volumeService.attachVolume(LinodeId.from(linodeId), VolumeId.from(volumeId));
      serverGenerator.install(LinodeId.from(linodeId), SensitiveString.of(password), ServerType.MINECRAFT);
      return null;
    }, Executors.newVirtualThreadPerTaskExecutor());

    redirectAttrs.addFlashAttribute("message", "Minecraft server is being installed");
    return "redirect:/linode";
  }

  @GetMapping("/create")
  String create(Model model) {
    model.addAttribute("serverTypes", List.of(ServerType.values()));

    return "linode/create";
  }

  @PostMapping("/create")
  String createResponse(@RequestParam ServerType serverType) {
    log.info("Creating server of type {}", serverType);

    serverGenerator.generate(getUsername(), serverType);
    return "redirect:/linode";
  }

  private static String getUsername() {
    String result = "unknown";
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      result = SecurityContextHolder.getContext().getAuthentication().getName();
    }
    return result;
  }
}
