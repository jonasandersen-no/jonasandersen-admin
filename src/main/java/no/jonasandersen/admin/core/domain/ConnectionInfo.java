package no.jonasandersen.admin.core.domain;

import no.jonasandersen.admin.core.minecraft.domain.Ip;

public record ConnectionInfo(String username, String password, Ip ip, int port) {

}
