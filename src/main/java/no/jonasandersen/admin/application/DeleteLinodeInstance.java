package no.jonasandersen.admin.application;

import java.util.Optional;
import java.util.function.UnaryOperator;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.application.port.ServerApi;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;

public class DeleteLinodeInstance {

  private final ServerApi api;

  private DeleteLinodeInstance(ServerApi api) {
    this.api = api;
  }

  public static DeleteLinodeInstance create(ServerApi api) {
    return new DeleteLinodeInstance(api);
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
    return configureForTest(UnaryOperator.identity());
  }

  public static DeleteLinodeInstance configureForTest(UnaryOperator<Config> configure) {
    Config config = configure.apply(new Config());
    return create(LinodeServerApi.configureForTest(config.linodeServerApiConfig));
  }

  public static class Config {

    private final LinodeServerApi.Config linodeServerApiConfig = new LinodeServerApi.Config();

    public Config addInstance(LinodeInstanceApi... instances) {
      linodeServerApiConfig.addInstance(instances);
      return this;
    }
  }
}
