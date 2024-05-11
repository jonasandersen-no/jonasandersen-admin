package no.jonasandersen.admin.core.domain;

public class InstanceNotFound extends RuntimeException {

  public InstanceNotFound(String message) {
    super(message);
  }
}
