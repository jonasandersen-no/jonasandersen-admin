package no.jonasandersen.admin.adapter.out.ssh;

public record ExecutedCommand(String output, int statusCode) {}
