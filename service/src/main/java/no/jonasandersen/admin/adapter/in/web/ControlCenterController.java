package no.jonasandersen.admin.adapter.in.web;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import no.jonasandersen.admin.application.ServerGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/control-center")
public class ControlCenterController {


  private final ServerGenerator serverGenerator;

  public ControlCenterController(ServerGenerator serverGenerator) {
    this.serverGenerator = serverGenerator;
  }

  @GetMapping
  String controlCenter() {
    return "control-center/index";
  }

  @PostMapping("/system-update")
  String systemUpdate(RedirectAttributes redirectAttributes) throws JSchException, IOException, InterruptedException {
    String result = serverGenerator.generateViaAnsible(
        "cd dev/ansible-control && ansible-playbook playbooks/update-system.yaml");

    redirectAttributes.addFlashAttribute("message", result);
    return "redirect:/control-center";
  }

}
