package no.jonasandersen.admin.core.minecraft;

import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;

public class MinecraftService {
private final ServerApi serverApi;

  public MinecraftService(ServerApi serverApi) {
    this.serverApi = serverApi;
  }

  public MinecraftInstance findMinecraftInstance() {
    return serverApi.listServerInfo();
  }

  public void startMinecraftInstance() {

  }
}
