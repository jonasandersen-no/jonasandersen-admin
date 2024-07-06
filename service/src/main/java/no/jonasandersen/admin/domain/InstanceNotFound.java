package no.jonasandersen.admin.domain;

public class InstanceNotFound extends RuntimeException {

  public InstanceNotFound(String message) {
    super(message);
  }
}
