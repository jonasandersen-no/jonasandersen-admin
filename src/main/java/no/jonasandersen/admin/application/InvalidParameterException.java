package no.jonasandersen.admin.application;

public class InvalidParameterException extends RuntimeException {

  public InvalidParameterException(String message) {
    super(message);
  }
}
