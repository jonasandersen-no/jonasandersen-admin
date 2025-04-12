package no.jonasandersen.admin.adapter.out.store;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "events")
public class JdbcEvent {
  private UUID aggregateRootId;
  private Instant recordedAt;
  private Integer eventId;
  private String eventType;
  private String json;
}
