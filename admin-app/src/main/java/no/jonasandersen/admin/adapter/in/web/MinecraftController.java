package no.jonasandersen.admin.adapter.in.web;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/minecraft")
public class MinecraftController {

  private final MinecraftHtmlFormatter formatter;

  public MinecraftController(MinecraftHtmlFormatter formatter) {
    this.formatter = formatter;
  }

  @GetMapping
  @HxRequest
  public String getMinecraft() {
    return formatter.format(new MinecraftInstance("Test", "127.0.0.1"));
  }
}
