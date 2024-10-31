package no.jonasandersen.admin.infrastructure;

import com.jcraft.jsch.JSchException;
import no.jonasandersen.admin.adapter.out.linode.LinodeExchange;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.ssh.FileExecutor;
import no.jonasandersen.admin.application.AccessControl;
import no.jonasandersen.admin.application.ControlCenterProperties;
import no.jonasandersen.admin.application.DefaultAccessControl;
import no.jonasandersen.admin.application.DeleteLinodeInstance;
import no.jonasandersen.admin.application.LinodeService;
import no.jonasandersen.admin.application.LinodeVolumeService;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.application.port.AccessControlRepository;
import no.jonasandersen.admin.application.port.ServerApi;
import no.jonasandersen.admin.application.port.UserSettingsRepository;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.infrastructure.AdminProperties.ControlCenter;
import no.jonasandersen.admin.infrastructure.AdminProperties.Linode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class CoreConfiguration {

  private static final Logger log = LoggerFactory.getLogger(CoreConfiguration.class);

  @Bean
  ThemeService themeService(UserSettingsRepository repository) {
    return ThemeService.create(repository);
  }

  @Bean
  LinodeService minecraftService(ServerApi serverApi, LinodeVolumeService linodeVolumeService) {
    return LinodeService.create(serverApi, linodeVolumeService);
  }

  @Bean
  LinodeVolumeService linodeVolumeService(ServerApi serverApi) {
    return LinodeVolumeService.create(serverApi);
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
  ServerGenerator serverGenerator(LinodeService linodeService, AdminProperties properties,
      ControlCenterProperties controlCenterProperties) throws JSchException {
    String rootPassword = properties.linode().rootPassword();
    return ServerGenerator.create(linodeService, SensitiveString.of(rootPassword), FileExecutor.create(),
        controlCenterProperties);
  }

  @Bean
  LinodeExchange linodeExchange(AdminProperties properties) {
    Linode linode = properties.linode();
    RestClient restClient = RestClient.builder()
        .baseUrl(linode.baseUrl())
        .requestInitializer(request -> request.getHeaders().setBearerAuth(linode.token()))
        .build();

    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

    return factory.createClient(LinodeExchange.class);
  }

  @Bean
  DeleteLinodeInstance deleteLinodeInstance(ServerApi serverApi) {
    return DeleteLinodeInstance.create(serverApi);
  }

  @Bean
  ControlCenterProperties controlCenterProperties(AdminProperties properties) {
    ControlCenter controlCenter = properties.controlCenter();
    return ControlCenterProperties.withKey(
        controlCenter.username(),
        SensitiveString.of(controlCenter.privateKeyString()),
        controlCenter.ip(),
        controlCenter.port()
    );
  }

  @Bean
  AccessControl accessControl(AccessControlRepository accessControlRepository) {
    return new DefaultAccessControl(accessControlRepository);
  }
}
