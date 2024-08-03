package no.jonasandersen.admin.adapter.out.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface CrudUserSettingsRepository extends JpaRepository<UserSettingsDbo, Long> {

  @Query("SELECT s FROM UserSettingsDbo s WHERE s.user.username = :username")
  Optional<UserSettingsDbo> findByUsername(String username);
}
