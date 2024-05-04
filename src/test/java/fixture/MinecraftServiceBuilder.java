package fixture;

import no.jonasandersen.admin.core.minecraft.LinodeVolumeService;
import no.jonasandersen.admin.core.minecraft.InMemoryServerApi;
import no.jonasandersen.admin.core.minecraft.LinodeService;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;

public class MinecraftServiceBuilder {

  private ServerApi serverApi = new InMemoryServerApi();

  private MinecraftServiceBuilder() {
  }

  public LinodeService build() {
    return new LinodeService(serverApi,
        new LinodeVolumeService(serverApi));
  }

  public static MinecraftServiceBuilder builder() {
    return new MinecraftServiceBuilder();
  }

  public MinecraftServiceBuilder withServerApi(ServerApi serverApi) {
    this.serverApi = serverApi;
    return this;
  }

  public ServerApi getServerApi() {
    return serverApi;
  }
}
