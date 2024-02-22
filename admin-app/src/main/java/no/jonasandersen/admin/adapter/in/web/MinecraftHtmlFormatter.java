package no.jonasandersen.admin.adapter.in.web;

import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;

public class MinecraftHtmlFormatter {

  public String format(MinecraftInstance minecraftInstance) {
    return """
        <p> Name: %s </p>
        <p> IP: %s </p>
        """.formatted(minecraftInstance.getName(), minecraftInstance.getIp());
  }
}
