package no.jonasandersen.admin;

import no.jonasandersen.admin.core.minecraft.MinecraftService;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfiguration {

  @Bean
  MinecraftService minecraftService(ServerApi serverApi) {
    return new MinecraftService(serverApi);
  }
}
