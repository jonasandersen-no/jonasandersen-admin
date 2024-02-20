package no.jonasandersen.admin.core.minecraft.domain;

import org.jilt.Builder;

@Builder
public class MinecraftInstance {

  private String name;

  public MinecraftInstance() {
  }

  public MinecraftInstance(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
