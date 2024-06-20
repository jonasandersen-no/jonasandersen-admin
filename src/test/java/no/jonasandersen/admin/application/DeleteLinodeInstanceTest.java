package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.domain.LinodeId;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class DeleteLinodeInstanceTest {

  @Test
  void executeDeletesExistingInstance() {
    LinodeInstanceApi instanceApi = Instancio.of(LinodeInstanceApi.class)
        .set(Select.field("id"), 1L)
        .create();
    LinodeServerApi api = LinodeServerApi.createNull(List.of(instanceApi));
    DeleteLinodeInstance usecase = new DeleteLinodeInstance(api);

    boolean deleted = usecase.delete(LinodeId.from(1L));
    assertThat(deleted).isTrue();
  }
}