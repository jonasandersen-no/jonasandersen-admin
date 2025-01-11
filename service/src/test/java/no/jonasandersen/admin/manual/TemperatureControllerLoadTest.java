package no.jonasandersen.admin.manual;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.org.awaitility.Awaitility;

@Tag("manual")
class TemperatureControllerLoadTest {

  public static final String URL = "http://localhost:8080/api/temperature";

  @Test
  void run() throws URISyntaxException, IOException, InterruptedException {
    String temperature = "42";

    for (int i = 0; i < 5; i++) {
      send(i + temperature, String.valueOf(new Random().nextInt(10)));
      Awaitility.waitAtMost(Duration.ofSeconds(1));
    }
  }

  private void send(String temperature, String humidity)
      throws URISyntaxException, IOException, InterruptedException {
    try (HttpClient client = HttpClient.newHttpClient()) {

      HttpRequest request = HttpRequest.newBuilder(new URI(URL))
          .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
          .POST(BodyPublishers.ofString(generateBody(temperature, humidity)))
          .build();

      client.send(request, HttpResponse.BodyHandlers.discarding());
    }
  }


  String generateBody(String temperature, String humidity) {
    String adjustedTemperature = ensureFiveDigitTemperature(temperature);

    return """
        {
          "objecttag": "test",
          "sensors": [
            {
              "sensortag": "mC",
              "sensorvalue": %s
            },
            {
              "sensortag": "Humidity",
              "sensorvalue": %s
            }
          ]
        }
        """.formatted(adjustedTemperature, humidity);
  }

  /**
   * Ensures the given temperature string is exactly 5 characters long and contains only numbers. If the string is
   * longer or shorter, it adjusts it to be 5 characters long.
   *
   * @param temperature the original temperature string
   * @return the adjusted temperature string
   */
  String ensureFiveDigitTemperature(String temperature) {
    String adjustedTemperature = temperature.replaceAll("[^0-9]", "");
    if (adjustedTemperature.length() < 5) {
      adjustedTemperature = adjustedTemperature + "0".repeat(5 - adjustedTemperature.length());
    } else if (adjustedTemperature.length() > 5) {
      adjustedTemperature = adjustedTemperature.substring(0, 5);
    }
    return adjustedTemperature;
  }
}
