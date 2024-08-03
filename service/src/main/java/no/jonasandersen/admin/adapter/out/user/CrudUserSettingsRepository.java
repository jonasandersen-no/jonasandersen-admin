package no.jonasandersen.admin.adapter.out.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudUserSettingsRepository extends JpaRepository<UserSettingsDbo, Long> {

  Optional<UserSettingsDbo> findByUsername(String username);
}
