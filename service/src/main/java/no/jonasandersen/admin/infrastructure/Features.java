package no.jonasandersen.admin.infrastructure;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import no.jonasandersen.admin.domain.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Features {

  private final static Map<Feature, Boolean> features = new EnumMap<>(Feature.class);
  private static final Logger log = LoggerFactory.getLogger(Features.class);

  public Features(AdminProperties adminProperties) {
    features.putAll(adminProperties.features());

    features.entrySet().stream().filter(Entry::getValue)
        .forEach(entry -> log.info("FEATURE {} IS ENABLED", entry.getKey()));

    features.entrySet().stream().filter(entry -> !entry.getValue())
        .forEach(entry -> log.info("FEATURE {} IS DISABLED", entry.getKey()));
  }

  public static boolean isEnabled(Feature feature) {
    return features.getOrDefault(feature, feature.defaultValue());
  }

  public static void setFeature(Feature feature, boolean enabled) {
    features.put(feature, enabled);
  }

  public static void reset() {
    features.clear();
  }

  public static Map<Feature, Boolean> getAllFeatures() {
    return Map.copyOf(features);
  }
}
