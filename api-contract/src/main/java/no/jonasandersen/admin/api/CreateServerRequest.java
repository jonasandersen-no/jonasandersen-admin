package no.jonasandersen.admin.api;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record CreateServerRequest(@NotNull ServerType serverType) {

}
