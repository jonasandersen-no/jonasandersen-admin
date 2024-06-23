package no.jonasandersen.admin.application;

import java.util.Optional;
import java.util.function.Function;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.application.port.ServerApi;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import org.springframework.stereotype.Service;

@Service
public class DeleteLinodeInstance {

  private final ServerApi api;

  private DeleteLinodeInstance(ServerApi api) {
    this.api = api;
  }

  public boolean delete(LinodeId linodeId) {
    Optional<LinodeInstance> instance = api.findInstanceById(linodeId);

    if (instance.isPresent()) {
      api.deleteInstance(linodeId);
      return true;
    }
    return false;
  }

  public static DeleteLinodeInstance configureForTest() {
    return configureForTest(Function.identity());
  }

  public static DeleteLinodeInstance configureForTest(Function<Config, Config> configure) {
    Config config = configure.apply(new Config());
    return new DeleteLinodeInstance(LinodeServerApi.configureForTest(config.linodeServerApiConfig));
  }

  public static class Config {

    private final LinodeServerApi.Config linodeServerApiConfig = new LinodeServerApi.Config();

    public Config addInstance(LinodeInstanceApi... instances) {
      linodeServerApiConfig.addInstance(instances);
      return this;
    }
  }
}
