package no.jonasandersen.admin.core.minecraft.domain;

import org.jilt.Builder;

@Builder
public class MinecraftInstance {

  private String name;
  private String ip;

  public MinecraftInstance() {
  }

  public MinecraftInstance(String name, String ip) {
    this.name = name;
    this.ip = ip;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }
}
