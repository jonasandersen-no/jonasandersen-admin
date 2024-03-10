package no.jonasandersen.admin.adapter.out.linode;

import no.jonasandersen.admin.core.minecraft.domain.Ip;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class LinodeServerApi implements ServerApi {

  private final LinodeExchange linodeExchange;
  private final Logger logger = LoggerFactory.getLogger(LinodeServerApi.class);

  LinodeServerApi(LinodeExchange linodeExchange) {
    this.linodeExchange = linodeExchange;
  }

  @Override
  public MinecraftInstance listServerInfo() {
    var result = linodeExchange.listInstances();

    if (result.isEmpty()) {
      logger.info("No instances found");
      return new MinecraftInstance();
    }

    Instance first = result.getFirst();

    MinecraftInstance minecraftInstance = new MinecraftInstance();
    minecraftInstance.setName(first.label());
    minecraftInstance.setIp(new Ip(first.ip()));
    minecraftInstance.setStatus(first.status());
    return minecraftInstance;
  }
}
