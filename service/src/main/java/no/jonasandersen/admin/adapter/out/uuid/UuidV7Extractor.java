package no.jonasandersen.admin.adapter.out.uuid;

import java.time.Instant;
import java.util.UUID;

/**
 * Utility class for extracting timestamps from UUID Version 7.
 *
 * <p>Provides methods to retrieve the embedded timestamp either as a java.time.Instant or as
 * milliseconds since the Unix epoch. Includes validation to ensure the input UUID is indeed version
 * 7.
 *
 * <p>Generated with assistance from Google Gemini on April 11, 2025.
 */
public class UuidV7Extractor {

  /**
   * Extracts the timestamp from a Version 7 UUID.
   *
   * <p>UUID v7 Structure (128 bits total): - 48 bits: Unix Timestamp in milliseconds (epoch Jan 1,
   * 1970 UTC) - 4 bits: Version (must be 0111 for v7) - 12 bits: Random sequence part 1 (rand_a) -
   * 2 bits: Variant (must be 10 for RFC 4122) - 6 bits: Random sequence part 2 (rand_b part 1) - 56
   * bits: Random sequence part 2 (rand_b part 2)
   *
   * <p>This method retrieves the first 48 bits from the most significant 64 bits of the UUID.
   *
   * @param uuid The Version 7 UUID.
   * @return An Instant object representing the timestamp encoded in the UUID.
   * @throws IllegalArgumentException if the provided UUID is null or not version 7.
   */
  public static Instant getTimestampFromUUIDv7(UUID uuid) {
    if (uuid == null) {
      throw new IllegalArgumentException("Input UUID cannot be null.");
    }

    // 1. Get the Most Significant Bits (MSB)
    long msb = uuid.getMostSignificantBits();

    // 2. Optional but recommended: Validate the version is 7
    // Version bits are bits 12-15 counting from the right (0-indexed) of the MSB.
    // Shift right by 12, then mask with 0xF (binary 1111) to isolate version bits.
    int version = (int) ((msb >> 12) & 0xF);
    if (version != 7) {
      throw new IllegalArgumentException("Input UUID is not version 7. Actual version: " + version);
      // Or you could return null or Optional.empty() if preferred over throwing
    }

    // 3. Extract the timestamp
    // The timestamp occupies the highest 48 bits of the MSB.
    // Right-shift the MSB by (64 - 48) = 16 bits to get the timestamp value.
    // Use unsigned right shift (>>>) to ensure the sign bit isn't extended.
    long timestampMillis = msb >>> 16;

    // 4. Convert milliseconds since epoch to an Instant
    return Instant.ofEpochMilli(timestampMillis);
  }

  /**
   * Extracts the timestamp from a Version 7 UUID as milliseconds since epoch.
   *
   * @param uuid The Version 7 UUID.
   * @return The timestamp encoded in the UUID as milliseconds since the Unix epoch.
   * @throws IllegalArgumentException if the provided UUID is null or not version 7.
   */
  public static long getTimestampMillisFromUUIDv7(UUID uuid) {
    if (uuid == null) {
      throw new IllegalArgumentException("Input UUID cannot be null.");
    }
    long msb = uuid.getMostSignificantBits();
    int version = (int) ((msb >> 12) & 0xF);
    if (version != 7) {
      throw new IllegalArgumentException("Input UUID is not version 7. Actual version: " + version);
    }
    // Extract the 48-bit timestamp (highest 48 bits of msb)
    return msb >>> 16;
  }
}
