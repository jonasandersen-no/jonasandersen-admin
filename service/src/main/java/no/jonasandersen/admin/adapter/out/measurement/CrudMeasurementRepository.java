package no.jonasandersen.admin.adapter.out.measurement;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudMeasurementRepository extends ListCrudRepository<MeasurementDbo, Long> {

}
