package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.adapter.UsernameResolver;
import no.jonasandersen.admin.application.DeleteLinodeInstance;
import no.jonasandersen.admin.application.LinodeService;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.domain.InstanceNotFound;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.domain.Subdomain;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/linode")
@Secured("ROLE_SERVER")
public class LinodeController {

  private static final Logger log = LoggerFactory.getLogger(LinodeController.class);
  public static final String INFO_MESSAGE_VARIABLE = "message";
  public static final String REDIRECT_LINODE = "redirect:/linode";
  private final AdminProperties properties;
  private final ServerGenerator serverGenerator;
  private final LinodeService service;
  private final ApplicationEventPublisher events;
  private final DeleteLinodeInstance deleteLinodeInstance;

  public LinodeController(
      AdminProperties properties,
      ServerGenerator serverGenerator,
      LinodeService service,
      ApplicationEventPublisher events,
      DeleteLinodeInstance deleteLinodeInstance) {
    this.properties = properties;
    this.serverGenerator = serverGenerator;
    this.service = service;
    this.events = events;
    this.deleteLinodeInstance = deleteLinodeInstance;
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
    } catch (HttpClientErrorException | InstanceNotFound e) {
      log.error("Failed to get instance with id {}", linodeId);
      log.error(e.getMessage());
      redirectAttrs.addFlashAttribute(
          INFO_MESSAGE_VARIABLE, "Failed to get instance with id " + linodeId);
      return REDIRECT_LINODE;
    }

    if (instance.isEmpty()) {
      return REDIRECT_LINODE;
    }

    model.addAttribute("instance", instance.get());
    return "linode/linode-detail";
  }

  @DeleteMapping("/{linodeId}")
  @Transactional
  public String deleteInstance(@PathVariable Long linodeId, RedirectAttributes redirectAttrs) {
    if (deleteLinodeInstance.delete(LinodeId.from(linodeId))) {
      redirectAttrs.addFlashAttribute(INFO_MESSAGE_VARIABLE, "Instance deleted");
      // Remove subdomain from DNS?
    }

    return REDIRECT_LINODE;
  }

  @GetMapping("/create")
  String create(Model model) {
    model.addAttribute("serverTypes", List.of(ServerType.values()));

    return "linode/create";
  }

  @PostMapping("/create")
  String createResponse(@RequestParam ServerType serverType, @RequestParam String subdomain) {
    subdomain = subdomain.replaceAll("[\n\r]", "_");

    if (subdomain.isBlank()) {
      log.info("Creating server of type {} with auto generated subdomain", serverType);
      serverGenerator.generate(UsernameResolver.getUsernameAsString(), serverType);
    } else {
      log.info("Creating server of type {} with subdomain '{}'", serverType, subdomain);
      serverGenerator.generate(
          UsernameResolver.getUsernameAsString(), serverType, Subdomain.of(subdomain));
    }
    return REDIRECT_LINODE;
  }
}
