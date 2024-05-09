package no.jonasandersen.admin.application;

public class ServerGenerator {


  public static ServerGenerator create() {
    return new ServerGenerator();
  }

  public static ServerGenerator createNull() {
    return new ServerGenerator();
  }

  private ServerGenerator() {
  }

  public Object generate() {
    return new Object();
  }
}
