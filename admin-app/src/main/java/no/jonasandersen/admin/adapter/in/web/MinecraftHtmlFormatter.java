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

    return STR."""
        <p> Name: \{minecraftInstance.getName()} </p>
        <p> IP: \{minecraftInstance.getIp().value()} </p>
        <p> Status: \{minecraftInstance.getStatus()} </p>
        """;
  }

  private static String handleError(Errors errors) {
    List<String> errorsHtml = new ArrayList<>();
    List<FieldError> fieldErrors = errors.getFieldErrors();
    for (FieldError error : fieldErrors) {
      errorsHtml.add(STR."<p>\{error.getField()}: \{error.getDefaultMessage()}</p>");
    }
    return STR."""
        <h2> Failed to validate \{fieldErrors.getFirst().getObjectName()} </h2>
        \{String.join("", errorsHtml)}
        """;
  }

}
