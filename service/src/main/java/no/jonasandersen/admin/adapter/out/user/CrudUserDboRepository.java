package no.jonasandersen.admin.adapter.out.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CrudUserDboRepository extends CrudRepository<UserDbo, Long> {

  Optional<UserDbo> findByUsername(String username);

  boolean existsByUsername(String username);

  @Query("select u.id from UserDbo u where u.username = ?1")
  Long findIdByUsername(String username);
}
