package no.jonasandersen.admin.domain;

public record SaveFileCreatedEvent(SaveFileId id, String name) implements SaveFileEvent {}
