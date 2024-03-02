package no.jonasandersen.admin.adapter.out.linode;

import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import org.springframework.stereotype.Service;

@Service
class LinodeServerApi implements ServerApi {

  private final LinodeExchange linodeExchange;

  LinodeServerApi(LinodeExchange linodeExchange) {
    this.linodeExchange = linodeExchange;
  }

  @Override
  public String listServerInfo() {
    String result = linodeExchange.listInstances();

    return "Server info";
  }
}
