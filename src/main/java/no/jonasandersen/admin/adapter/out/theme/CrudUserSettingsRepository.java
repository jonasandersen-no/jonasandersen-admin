package no.jonasandersen.admin.adapter.out.theme;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudUserSettingsRepository extends CrudRepository<UserSettingsDbo, Long> {

  Optional<UserSettingsDbo> findByUsername(String username);
}
