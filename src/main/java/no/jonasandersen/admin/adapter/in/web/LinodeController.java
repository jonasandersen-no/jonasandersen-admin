package no.jonasandersen.admin.adapter.in.web;

import com.panfutov.result.GenericError;
import com.panfutov.result.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import no.jonasandersen.admin.application.DnsService;
import no.jonasandersen.admin.application.LinodeService;
import no.jonasandersen.admin.application.LinodeVolumeService;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerGeneratorResponse;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.domain.VolumeId;
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
  private final DnsService dnsService;

  public LinodeController(AdminProperties properties, ServerGenerator serverGenerator,
      LinodeVolumeService volumeService, LinodeService service, DnsService dnsService) {
    this.properties = properties;
    this.serverGenerator = serverGenerator;
    this.volumeService = volumeService;
    this.service = service;
    this.dnsService = dnsService;
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
      redirectAttrs.addFlashAttribute("message", RedirectResponse.of("Instance is not running"));
      return "redirect:/linode";
    }

    String password = properties.minecraft().password();
    Long volumeId = properties.linode().volumeId();
    CompletableFuture.supplyAsync(() -> {
      volumeService.attachVolume(LinodeId.from(linodeId), VolumeId.from(volumeId));
      serverGenerator.install(LinodeId.from(linodeId), SensitiveString.of(password),
          ServerType.MINECRAFT);
      return null;
    }, Executors.newVirtualThreadPerTaskExecutor());

    redirectAttrs.addFlashAttribute("message",
        RedirectResponse.of("Minecraft server is being installed"));
    return "redirect:/linode";
  }

  @GetMapping("/create")
  String create(Model model) {
    model.addAttribute("serverTypes", List.of(ServerType.values()));

    return "linode/create";
  }

  @PostMapping("/create")
  String createResponse(@RequestParam ServerType serverType, RedirectAttributes redirectAttrs) {
    log.info("Creating server of type {}", serverType);

    Result<ServerGeneratorResponse> serverGeneratorresult = serverGenerator.generate(getUsername(), serverType);

    if (serverGeneratorresult.isFailure()) {
      List<String> messages = new ArrayList<>();
      log.warn("Failed to create server of type {}", serverType);
      messages.add("Failed to create server of type " + serverType);
      for (GenericError error : serverGeneratorresult.getErrors()) {
        messages.add(error.getMessage());
        log.warn(error.getMessage());
      }
      redirectAttrs.addFlashAttribute("message", RedirectResponse.of(messages));
      return "redirect:/linode";
    }

    ServerGeneratorResponse response = serverGeneratorresult.getObject();
    Result<Void> result = dnsService.createOrReplaceRecord(response.ip(), getUsername(), "");

    List<String> messages = new ArrayList<>();
    if (result.hasErrors()) {
      messages.add("Server was created but creating/replacing DNS record failed");
      for (GenericError error : result.getErrors()) {
        messages.add(error.getMessage());
        log.warn(error.getMessage());
      }
    } else {
      messages.add("Server was created successfully");
    }

    redirectAttrs.addFlashAttribute("message", RedirectResponse.of(messages));

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
