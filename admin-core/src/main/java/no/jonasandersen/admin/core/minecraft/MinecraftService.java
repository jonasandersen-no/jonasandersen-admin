package no.jonasandersen.admin.core.minecraft;

import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstanceBuilder;

public class MinecraftService {

  public MinecraftInstance findMinecraftInstance() {
    return MinecraftInstanceBuilder.minecraftInstance()
        .name("minecraft")
        .build();
  }
}
