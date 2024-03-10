package no.jonasandersen.admin.core.minecraft.port;

import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;

public interface ServerApi {

  MinecraftInstance listServerInfo();
}
