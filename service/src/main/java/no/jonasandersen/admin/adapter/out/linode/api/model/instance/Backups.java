package no.jonasandersen.admin.adapter.out.linode.api.model.instance;

import java.time.LocalDateTime;

public record Backups(
    boolean enabled, boolean available, Schedule schedule, LocalDateTime lastSuccessful) {}
