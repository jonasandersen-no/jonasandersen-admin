package no.jonasandersen.admin.adapter.out.savefile;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdbcSaveFileRepository extends ListCrudRepository<SaveFileDbo, Long> {}
