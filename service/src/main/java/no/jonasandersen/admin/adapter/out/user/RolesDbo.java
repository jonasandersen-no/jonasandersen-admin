package no.jonasandersen.admin.adapter.out.user;

import no.jonasandersen.admin.domain.Roles;

enum RolesDbo {
  USER,
  SERVER,
  ADMIN;

  static RolesDbo fromDomain(Roles roles) {
    return switch (roles) {
      case USER -> RolesDbo.USER;
      case SERVER -> RolesDbo.SERVER;
      case ADMIN -> RolesDbo.ADMIN;
      case null -> throw new IllegalArgumentException("Roles cannot be null");
    };
  }

  static Roles toDomain(RolesDbo rolesDbo) {
    return switch (rolesDbo) {
      case USER -> Roles.USER;
      case SERVER -> Roles.SERVER;
      case ADMIN -> Roles.ADMIN;
    };
  }
}
