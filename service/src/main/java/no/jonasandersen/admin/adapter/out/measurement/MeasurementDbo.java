package no.jonasandersen.admin.adapter.out.measurement;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "measurement_dbo")
public class MeasurementDbo {

  @Id private Long id;

  private String temperature;

  private int humidity;

  private LocalDateTime timestamp;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public int getHumidity() {
    return humidity;
  }

  public void setHumidity(int humidity) {
    this.humidity = humidity;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "MeasurementDbo{"
        + "id="
        + id
        + ", temperature='"
        + temperature
        + '\''
        + ", humidity="
        + humidity
        + ", timestamp="
        + timestamp
        + '}';
  }
}
