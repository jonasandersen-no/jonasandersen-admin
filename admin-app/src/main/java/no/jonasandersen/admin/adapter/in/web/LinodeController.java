package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.minecraft.MinecraftService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/linode")
public class LinodeController {

  private final MinecraftService service;

  public LinodeController(MinecraftService service) {
    this.service = service;
  }

  @GetMapping
  String linode(Model model) {
    List<LinodeInstance> instances = service.getInstances();
    model.addAttribute("instances", instances);
    return "linode-new";
  }
}
