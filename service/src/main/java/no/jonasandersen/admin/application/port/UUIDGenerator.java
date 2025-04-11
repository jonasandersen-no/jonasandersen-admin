package no.jonasandersen.admin.application.port;

import com.fasterxml.uuid.Generators;
import java.time.Instant;
import java.util.UUID;
import no.jonasandersen.admin.adapter.out.uuid.UuidV7Extractor;

public class UUIDGenerator {

  private UUIDGenerator() {}

  public static UUID generate() {
    return Generators.timeBasedEpochGenerator().generate();
  }

  public static Instant getTime(UUID uuid) {
    return UuidV7Extractor.getTimestampFromUUIDv7(uuid);
  }
}
