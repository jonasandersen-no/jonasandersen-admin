package no.jonasandersen.admin.infrastructure;

import java.util.List;
import no.jonasandersen.admin.adapter.DefaultEventPublisher;
import no.jonasandersen.admin.adapter.out.database.shortcut.JdbcShortcutRepository;
import no.jonasandersen.admin.adapter.out.linode.JpaLinodeInstanceRepository;
import no.jonasandersen.admin.adapter.out.linode.LinodeExchange;
import no.jonasandersen.admin.adapter.out.linode.LinodeInstanceDatabaseRepository;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.theme.CrudUserSettingsRepository;
import no.jonasandersen.admin.adapter.out.theme.DefaultUserSettingsRepository;
import no.jonasandersen.admin.application.Feature;
import no.jonasandersen.admin.application.Features;
import no.jonasandersen.admin.application.LinodeService;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.application.port.EventPublisher;
import no.jonasandersen.admin.application.port.UserSettingsRepository;
import no.jonasandersen.admin.core.minecraft.LinodeVolumeService;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import no.jonasandersen.admin.core.shortcut.port.Broadcaster;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;
import no.jonasandersen.admin.domain.SensitiveString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;

@Configuration
class CoreConfiguration {

  private static final Logger log = LoggerFactory.getLogger(CoreConfiguration.class);

  @Bean
  ThemeService themeService(UserSettingsRepository repository) {
    return ThemeService.create(repository);
  }

  @Bean
  LinodeService minecraftService(ServerApi serverApi, LinodeVolumeService linodeVolumeService,
      EventPublisher eventPublisher, LinodeInstanceDatabaseRepository repository) {
    return LinodeService.create(serverApi, linodeVolumeService, eventPublisher, repository);
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
    return LinodeVolumeService.create(serverApi);
  }

  @Bean
  @Transactional
  DefaultUserSettingsRepository userSettingsRepository(CrudUserSettingsRepository repository) {
    return DefaultUserSettingsRepository.create(repository);
  }

  @Bean
  @DependsOn("features")
  ServerApi linodeServerApi(LinodeExchange linodeExchange) {
    if (Features.isEnabled(Feature.LINODE_STUB)) {
      log.info("Using stub LinodeServerApi");
      return LinodeServerApi.createNull();
    }

    return LinodeServerApi.create(linodeExchange);
  }

  @Bean
  @Transactional
  LinodeInstanceDatabaseRepository databaseServerApi(JpaLinodeInstanceRepository repository) {
    return LinodeInstanceDatabaseRepository.create(repository);
  }

  @Bean
  ServerGenerator serverGenerator(LinodeService linodeService, AdminProperties properties) {
    String rootPassword = properties.linode().rootPassword();
    return ServerGenerator.create(linodeService, SensitiveString.of(rootPassword));
  }

  @Bean
  DefaultEventPublisher eventPublisher(ApplicationEventPublisher publisher) {
    return DefaultEventPublisher.create(publisher);
  }
}
