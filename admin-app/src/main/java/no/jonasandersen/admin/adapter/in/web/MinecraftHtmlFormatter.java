package no.jonasandersen.admin.adapter.in.web;

import java.util.ArrayList;
import java.util.List;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

@Component
public class MinecraftHtmlFormatter {

  private final Validator validator;

  public MinecraftHtmlFormatter(Validator validator) {
    this.validator = validator;
  }

  public String format(MinecraftInstance minecraftInstance) {
    Errors errors = validator.validateObject(minecraftInstance);

    if (errors.hasErrors()) {
      return handleError(errors);
    }

    return """
        <p> Name: %s </p>
        <p> IP: %s </p>
        <p> Status: %s </p>
        """.formatted(minecraftInstance.getName(), minecraftInstance.getIp().value(),
        minecraftInstance.getStatus());
  }

  private static String handleError(Errors errors) {
    List<String> errorsHtml = new ArrayList<>();
    List<FieldError> fieldErrors = errors.getFieldErrors();
    for (FieldError error : fieldErrors) {
      errorsHtml.add("<p>" + error.getField() + ": " + error.getDefaultMessage() + "</p>");
    }
    return """
        <h2> Failed to validate %s </h2>
        %s
        """.formatted(fieldErrors.getFirst().getObjectName(), String.join("", errorsHtml));
  }

}
