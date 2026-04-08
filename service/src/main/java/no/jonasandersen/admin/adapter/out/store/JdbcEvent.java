package no.jonasandersen.admin.adapter.out.store;

import java.time.Instant;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "events")
public class JdbcEvent implements Persistable<UUID> {

  @Id
  private UUID eventId;
  private UUID aggregateRootId;
  private Long version;
  private Instant recordedAt;
  private String eventType;
  private String content;

  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public UUID getAggregateRootId() {
    return aggregateRootId;
  }

  public void setAggregateRootId(UUID aggregateRootId) {
    this.aggregateRootId = aggregateRootId;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Instant getRecordedAt() {
    return recordedAt;
  }

  public void setRecordedAt(Instant recordedAt) {
    this.recordedAt = recordedAt;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public @Nullable UUID getId() {
    return eventId;
  }

  @Override
  public boolean isNew() {
    return true;
  }
}
