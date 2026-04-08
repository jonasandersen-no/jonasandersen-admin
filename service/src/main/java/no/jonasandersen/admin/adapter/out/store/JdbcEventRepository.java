package no.jonasandersen.admin.adapter.out.store;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdbcEventRepository extends ListCrudRepository<JdbcEvent, String> {

  List<JdbcEvent> findByAggregateRootIdOrderByVersion(UUID aggregateRootId);
}
