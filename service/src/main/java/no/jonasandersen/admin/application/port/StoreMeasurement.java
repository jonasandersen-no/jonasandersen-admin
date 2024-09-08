package no.jonasandersen.admin.application.port;

import no.jonasandersen.admin.domain.Measurement;

public interface StoreMeasurement {

  void store(Measurement measurement);
}
