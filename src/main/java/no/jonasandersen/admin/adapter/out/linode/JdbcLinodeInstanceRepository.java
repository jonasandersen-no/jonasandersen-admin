package no.jonasandersen.admin.adapter.out.linode;

import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeInstanceDbo;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdbcLinodeInstanceRepository extends ListCrudRepository<LinodeInstanceDbo, Long> {

  @Query("select count(*) from linode_instance")
  long countAll();
}
