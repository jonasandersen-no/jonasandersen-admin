package no.jonasandersen.admin.adapter.in.web;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTrigger;
import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.minecraft.MinecraftService;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hx/minecraft")
class MinecraftHxController {

  private static final Logger logger = LoggerFactory.getLogger(MinecraftHxController.class);
  private final MinecraftHtmlFormatter formatter;
  private final MinecraftService service;
  private boolean isRunning;

  public MinecraftHxController(MinecraftHtmlFormatter formatter, MinecraftService service) {
    this.formatter = formatter;
    this.service = service;
  }

  @GetMapping
  @HxRequest
  public String getMinecraft() {
//    MinecraftInstance instance = service.findMinecraftInstance();
//    if (instance.isEmpty()) {
//      return """
//          <p> No server is running </p>
//          """;
//    }

    List<LinodeInstance> instances = service.getInstances();

    logger.info("Found {} instances", instances.size());
    instances.forEach(instance1 -> logger.info("{}", instance1));

    return "";
//
//    return formatter.format(instance);
  }

  @PostMapping
  @HxRequest
  @HxTrigger("my-event")
  public String postMinecraft() {
    MinecraftInstance minecraftInstance = service.findMinecraftInstance();

    isRunning = !isRunning;

    if (isRunning) {
      return "Start server";
    }

    service.startMinecraftInstance();
    return "Stop server";
//    return formatter.format(instance);
  }


}
