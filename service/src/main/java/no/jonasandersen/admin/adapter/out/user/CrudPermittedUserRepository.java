package no.jonasandersen.admin.adapter.out.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudPermittedUserRepository extends JpaRepository<PermittedUserDbo, Long> {

  boolean existsByEmail(String email);

  PermittedUserDbo findByEmail(String email);

  long deleteByEmail(String email);
}
