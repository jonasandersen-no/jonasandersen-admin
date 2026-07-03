package no.jonasandersen.admin.ssh;

public record ExecutedCommand(String output, int statusCode) {}
