package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import no.jonasandersen.admin.application.port.StoreMeasurement;
import no.jonasandersen.admin.domain.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/temperature")
public class TemperatureController {

  private static final Logger log = LoggerFactory.getLogger(TemperatureController.class);

  public TemperatureController(StoreMeasurement storeMeasurement) {
    this.storeMeasurement = storeMeasurement;
  }

  public record Sensor(String sensortag, String sensorvalue) {

  }

  public record Device(String objecttag, List<Sensor> sensors) {

  }

  private final StoreMeasurement storeMeasurement;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  void catchAll(@RequestBody Device device) {
    log.info("Received request: {}", device);

    Sensor temperature = device.sensors().stream().filter(sensor -> sensor.sensortag().equals("mC"))
        .findFirst().orElseThrow(() -> new IllegalArgumentException("No sensor with tag mC"));

    Sensor humidity = device.sensors().stream().filter(sensor -> sensor.sensortag().equals("Humidity"))
        .findFirst().orElseThrow(() -> new IllegalArgumentException("No sensor with tag Humidity"));

    storeMeasurement.store(new Measurement(temperature.sensorvalue(), humidity.sensorvalue()));
  }
}
