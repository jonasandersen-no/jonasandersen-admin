package no.jonasandersen.admin.core.minecraft.domain;


public class MinecraftInstance {

  public static final String IP_REGEX = "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";
  private String name;
  private String ip;
  private String status;

  public MinecraftInstance() {
  }

  public MinecraftInstance(String name, String ip) {
    this.name = name;

    if (!ip.matches(IP_REGEX)) {
      throw new IllegalArgumentException("Invalid IP address");
    }

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
    if (!ip.matches(IP_REGEX)) {
      throw new IllegalArgumentException("Invalid IP address");
    }
    this.ip = ip;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
