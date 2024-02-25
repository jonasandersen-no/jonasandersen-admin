package no.jonasandersen.admin.core.minecraft;

import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;

public class MinecraftService {

  public MinecraftInstance findMinecraftInstance() {
    return new MinecraftInstance("minecraft-auto-config-1", "127.0.0.1");
  }
}
