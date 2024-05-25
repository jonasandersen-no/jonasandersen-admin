package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.application.LinodeService;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.domain.ServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/linode")
public class LinodeController {

  private static final Logger log = LoggerFactory.getLogger(LinodeController.class);
  private final ServerGenerator serverGenerator;
  private final LinodeService service;

  public LinodeController(ServerGenerator serverGenerator, LinodeService service) {
    this.serverGenerator = serverGenerator;
    this.service = service;
  }

  @GetMapping()
  String linode(Model model) {
    model.addAttribute("instances", service.getInstances());
    return "linode/index";
  }

  @GetMapping("/{linodeId}")
  String getInstance(@PathVariable Long linodeId, Model model) {
    Optional<LinodeInstance> instance = service.findInstanceById(new LinodeId(linodeId));

    if (instance.isEmpty()) {
      return "redirect:/linode";
    }

    model.addAttribute("instance", instance.get());
    return "linode/linode-detail";
  }

  @PostMapping("/{linodeId}")
  String installMinecraft(@PathVariable Long linodeId, RedirectAttributes redirectAttrs) {
    serverGenerator.install(LinodeId.from(linodeId), ServerType.MINECRAFT);

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

    serverGenerator.generate(serverType);
    return "redirect:/linode";
  }
}
