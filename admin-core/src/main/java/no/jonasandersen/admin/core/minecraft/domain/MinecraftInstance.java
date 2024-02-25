package no.jonasandersen.admin.core.minecraft.domain;


public class MinecraftInstance {

  private String name;
  private Ip ip;
  private String status;

  public MinecraftInstance() {
  }

  public MinecraftInstance(String name, String ip) {
    this.name = name;

    this.ip = new Ip(ip);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Ip getIp() {
    return ip;
  }

  public void setIp(Ip ip) {
    this.ip = ip;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
