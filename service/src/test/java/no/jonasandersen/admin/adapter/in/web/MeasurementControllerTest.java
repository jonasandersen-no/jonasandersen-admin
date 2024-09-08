package no.jonasandersen.admin.adapter.in.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class MeasurementControllerTest extends IoBasedTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldPostTemperature() throws Exception {
    String jsonContent = """
        {
          "objecttag": "objecttag",
          "sensors": [
            {
              "sensortag": "mC",
              "sensorvalue": 12345
            },
            {
              "sensortag": "Humidity",
              "sensorvalue": 67
            }
          ]
        }
        """;

    mockMvc.perform(post("/api/temperature")
            .with(csrf())
            .contentType("application/json")
            .content(jsonContent))
        .andExpect(status().isCreated());
  }
}