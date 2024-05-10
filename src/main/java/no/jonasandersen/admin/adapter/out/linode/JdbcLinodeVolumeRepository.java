package no.jonasandersen.admin.adapter.out.linode;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeVolumeDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdbcLinodeVolumeRepository extends JpaRepository<LinodeVolumeDbo, Long> {

  List<LinodeVolumeDbo> findByLinodeId(Long id);
}
