package no.jonasandersen.admin;

import java.util.List;
import no.jonasandersen.admin.adapter.out.database.shortcut.JdbcShortcutRepository;
import no.jonasandersen.admin.adapter.out.linode.DatabaseServerApi;
import no.jonasandersen.admin.adapter.out.linode.JdbcLinodeInstanceRepository;
import no.jonasandersen.admin.adapter.out.linode.JdbcLinodeVolumeRepository;
import no.jonasandersen.admin.adapter.out.linode.LinodeExchange;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.theme.CrudUserSettingsRepository;
import no.jonasandersen.admin.adapter.out.theme.DefaultUserSettingsRepository;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.application.port.UserSettingsRepository;
import no.jonasandersen.admin.core.minecraft.LinodeService;
import no.jonasandersen.admin.core.minecraft.LinodeVolumeService;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import no.jonasandersen.admin.core.shortcut.port.Broadcaster;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;

@Configuration
class CoreConfiguration {

  @Bean
  ThemeService themeService(UserSettingsRepository repository) {
    return ThemeService.create(repository);
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

  @Bean
  @Transactional
  DefaultUserSettingsRepository userSettingsRepository(CrudUserSettingsRepository repository) {
    return DefaultUserSettingsRepository.create(repository);
  }

  @Bean
  @Profile("prod")
  ServerApi linodeServerApi(LinodeExchange linodeExchange) {
    return LinodeServerApi.create(linodeExchange);
  }

  @Bean
  @Profile("!prod")
  ServerApi databaseServerApi(JdbcLinodeInstanceRepository repository,
      JdbcLinodeVolumeRepository volumeRepository) {
    return new DatabaseServerApi(repository, volumeRepository);
  }
}
