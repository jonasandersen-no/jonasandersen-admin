package no.jonasandersen.admin.domain;

public class CommandExecutionFailedException extends RuntimeException {

  public CommandExecutionFailedException(Exception e) {
    super(e);
  }
}
