package no.jonasandersen.admin.adapter.in.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

class TemperatureControllerTest {

  private final MockMvcTester mvc = MockMvcTester.of(new TemperatureController(measurement -> {
  }));

  @Test
  void canPostTemperature() throws Exception {

    mvc.post().uri("/api/temperature").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "objecttag": "device123",
              "sensors": [
                {
                  "sensortag": "mC",
                  "sensorvalue": "12345"
                },
                {
                  "sensortag": "Humidity",
                  "sensorvalue": "60"
                }
              ]
            }
            """)
        .assertThat()
        .hasStatus(HttpStatus.CREATED);
  }

  @Test
  void missingCelsiusReturnsBadRequest() throws Exception {

    mvc.post().uri("/api/temperature").with(csrf())
        .content("""
            {
              "objecttag": "device123",
              "sensors": [
                {
                  "sensortag": "Humidity",
                  "sensorvalue": "60"
                }
              ]
            }
            """)
        .contentType("application/json")
        .assertThat()
        .hasStatus(HttpStatus.BAD_REQUEST);
  }
}