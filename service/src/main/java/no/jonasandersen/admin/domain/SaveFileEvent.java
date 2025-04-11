package no.jonasandersen.admin.domain;

public sealed interface SaveFileEvent extends Event permits SaveFileCreatedEvent {}
