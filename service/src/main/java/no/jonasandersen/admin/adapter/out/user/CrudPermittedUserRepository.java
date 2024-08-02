package no.jonasandersen.admin.adapter.out.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudPermittedUserRepository extends CrudRepository<PermittedUserDbo, Long> {

  boolean existsBySubjectAndEmail(String subject, String email);
}
