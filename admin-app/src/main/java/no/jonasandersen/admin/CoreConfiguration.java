package no.jonasandersen.admin;

import java.util.List;
import no.jonasandersen.admin.adapter.out.database.shortcut.JdbcShortcutRepository;
import no.jonasandersen.admin.core.LinodeVolumeService;
import no.jonasandersen.admin.core.minecraft.LinodeService;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import no.jonasandersen.admin.core.shortcut.port.Broadcaster;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;
import no.jonasandersen.admin.core.theme.UserSettingsService;
import no.jonasandersen.admin.core.theme.port.UserSettingsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
class CoreConfiguration {

  @Bean
  UserSettingsService userSettingsService(UserSettingsRepository repository) {
    return new UserSettingsService(repository);
  }

  @Bean
  LinodeService minecraftService(ServerApi serverApi, LinodeVolumeService linodeVolumeService) {
    return new LinodeService(serverApi, linodeVolumeService);
  }

  @Bean
  ShortcutService shortcutService(ShortcutRepository repository, List<Broadcaster> broadcasters) {
    return new ShortcutService(repository, broadcasters);
  }

  @Bean
  ShortcutRepository shortcutRepository(JdbcClient jdbcClient) {
    return new JdbcShortcutRepository(jdbcClient);
  }

  @Bean
  LinodeVolumeService linodeVolumeService(ServerApi serverApi) {
    return new LinodeVolumeService(serverApi);
  }
}
