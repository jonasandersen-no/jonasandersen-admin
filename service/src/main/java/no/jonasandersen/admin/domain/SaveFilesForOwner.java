package no.jonasandersen.admin.domain;

import java.util.List;

public record SaveFilesForOwner(String owner, List<String> saveFilesNames) {}
