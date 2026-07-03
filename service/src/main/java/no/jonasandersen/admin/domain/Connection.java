package no.jonasandersen.admin.domain;

public sealed interface Connection permits Hostname, Ip {

  String value();
}
