package no.jonasandersen.admin.api;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record CreateServerResponse(Long id, String label, @NotNull String ip, @NotNull String owner) {

}
