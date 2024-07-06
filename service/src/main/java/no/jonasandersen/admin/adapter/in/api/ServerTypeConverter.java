package no.jonasandersen.admin.adapter.in.api;

import no.jonasandersen.admin.domain.ServerType;

class ServerTypeConverter {

  private ServerTypeConverter() {
  }

  static ServerType convert(no.jonasandersen.admin.api.ServerType serverType) {
    return switch (serverType) {
      case DEFAULT -> ServerType.DEFAULT;
      case MINECRAFT -> ServerType.MINECRAFT;
    };
  }
}