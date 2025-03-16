package no.jonasandersen.admin.adapter.out.user;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudPermittedUserRepository extends ListCrudRepository<PermittedUserDbo, Long> {

  boolean existsByEmail(String email);

  PermittedUserDbo findByEmail(String email);

  @Modifying
  @Query("delete from permitted_users pu where pu.email = :email")
  long deleteByEmail(String email);
}
