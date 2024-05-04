package no.jonasandersen.admin.adapter.out.linode;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeInstanceDbo;
import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JdbcLinodeServiceRepositoryTest extends IoBasedTest {


  @Autowired
  JdbcLinodeInstanceRepository repository;

  @Test
  void save() {
    LinodeInstanceDbo saved = repository.save(
        new LinodeInstanceDbo(null, "127.0.0.1", "running", "saveIp",
            "tag", "volume"));

    assertThat(saved).isNotNull()
        .extracting(LinodeInstanceDbo::id)
        .isNotNull();
  }
}