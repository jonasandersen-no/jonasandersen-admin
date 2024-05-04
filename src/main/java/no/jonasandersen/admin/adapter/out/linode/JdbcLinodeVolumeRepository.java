package no.jonasandersen.admin.adapter.out.linode;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeVolumeDbo;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdbcLinodeVolumeRepository extends ListCrudRepository<LinodeVolumeDbo, Long> {

  @Query("SELECT * FROM linode_volume WHERE linode_id = :id")
  List<LinodeVolumeDbo> findByLinodeId(Long id);
}
