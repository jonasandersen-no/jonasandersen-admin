package no.jonasandersen.admin;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class EmptyValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return true;
  }

  @Override
  public void validate(Object target, Errors errors) {

  }
}
