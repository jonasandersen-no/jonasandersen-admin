package no.jonasandersen.admin.application;

import no.jonasandersen.admin.core.minecraft.domain.Ip;

public record ServerGeneratorResponse(String label, Ip ip) {

}
