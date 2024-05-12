package no.jonasandersen.admin.infrastructure;

import java.util.List;
import no.jonasandersen.admin.UseStubPredicate;
import no.jonasandersen.admin.adapter.out.database.shortcut.JdbcShortcutRepository;
import no.jonasandersen.admin.adapter.out.linode.DatabaseServerApi;
import no.jonasandersen.admin.adapter.out.linode.JdbcLinodeInstanceRepository;
import no.jonasandersen.admin.adapter.out.linode.JdbcLinodeVolumeRepository;
import no.jonasandersen.admin.adapter.out.linode.LinodeExchange;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.theme.CrudUserSettingsRepository;
import no.jonasandersen.admin.adapter.out.theme.DefaultUserSettingsRepository;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.application.port.UserSettingsRepository;
import no.jonasandersen.admin.application.LinodeService;
import no.jonasandersen.admin.core.minecraft.LinodeVolumeService;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import no.jonasandersen.admin.core.shortcut.port.Broadcaster;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;
import no.jonasandersen.admin.domain.SensitiveString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;

@Configuration
class CoreConfiguration {

  private static final Logger log = LoggerFactory.getLogger(CoreConfiguration.class);
  private final UseStubPredicate useStubPredicate;

  CoreConfiguration(UseStubPredicate useStubPredicate) {
    this.useStubPredicate = useStubPredicate;
  }

  @Bean
  ThemeService themeService(UserSettingsRepository repository) {
    return ThemeService.create(repository);
  }

  @Bean
  LinodeService minecraftService(ServerApi serverApi, LinodeVolumeService linodeVolumeService) {
    return LinodeService.create(serverApi, linodeVolumeService);
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
  ServerApi linodeServerApi(LinodeExchange linodeExchange) {
    if (useStubPredicate.test("linode")) {
      log.info("Using stub LinodeServerApi");
      return LinodeServerApi.createNull();
    }

    return LinodeServerApi.create(linodeExchange);
  }

  @Bean
  DatabaseServerApi databaseServerApi(JdbcLinodeInstanceRepository repository,
      JdbcLinodeVolumeRepository volumeRepository) {
    return new DatabaseServerApi(repository, volumeRepository);
  }

  @Bean
  ServerGenerator serverGenerator(LinodeService linodeService, AdminProperties properties) {
    String rootPassword = properties.linode().rootPassword();
    return ServerGenerator.create(linodeService, SensitiveString.of(rootPassword));
  }
}
