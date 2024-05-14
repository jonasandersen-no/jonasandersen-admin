package no.jonasandersen.admin.adapter.out.linode;

import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeInstanceDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinodeInstanceRepository extends JpaRepository<LinodeInstanceDbo, Long> {

  @Query("select count(*) from LinodeInstanceDbo")
  long countAll();
}
