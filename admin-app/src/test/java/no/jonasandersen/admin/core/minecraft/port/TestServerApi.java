package no.jonasandersen.admin.core.minecraft.port;


import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;

public class TestServerApi implements ServerApi {


  @Override
  public MinecraftInstance listServerInfo() {
    return new MinecraftInstance("minecraft-auto-config-1", "127.0.0.1");
  }
}

