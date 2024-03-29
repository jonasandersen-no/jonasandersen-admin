package no.jonasandersen.admin;

import java.util.List;
import no.jonasandersen.admin.adapter.out.database.shortcut.JdbcShortcutRepository;
import no.jonasandersen.admin.core.minecraft.MinecraftService;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import no.jonasandersen.admin.core.shortcut.port.Broadcaster;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class CoreConfiguration {

  @Bean
  MinecraftService minecraftService(ServerApi serverApi) {
    return new MinecraftService(serverApi);
  }

  @Bean
  ShortcutService shortcutService(ShortcutRepository repository, List<Broadcaster> broadcasters) {
    return new ShortcutService(repository, broadcasters);
  }

  @Bean
  ShortcutRepository shortcutRepository(JdbcClient jdbcClient) {
    return new JdbcShortcutRepository(jdbcClient);
  }
}
