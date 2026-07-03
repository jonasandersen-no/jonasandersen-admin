package no.jonasandersen.admin.domain;

public sealed interface ConnectionInfo permits PasswordConnectionInfo, PrivateKeyConnectionInfo {

  String username();

  SensitiveString credentials();

  Connection address();

  int port();
}
