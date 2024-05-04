package no.jonasandersen.admin.core.minecraft;

import static org.assertj.core.api.Assertions.assertThat;

import fixture.MinecraftServiceBuilder;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.minecraft.domain.Ip;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import org.junit.jupiter.api.Test;

public class LinodeServiceTest {

  private final LinodeService service;
  private final ServerApi serverApi;

  public LinodeServiceTest() {

    MinecraftServiceBuilder builder = MinecraftServiceBuilder.builder();

    serverApi = builder.getServerApi();

    service = builder.build();
  }

  @Test
  void minecraftIsInstalledWhenLinodeInstanceExists() {
    LinodeInstance instance = serverApi.createInstance("auto-created-1", "Minecraft");

    MinecraftInstance minecraftInstance = service.startMinecraftInstance(instance.linodeId());

    assertThat(minecraftInstance).isNotNull()
        .extracting(MinecraftInstance::getName, MinecraftInstance::getIp,
            MinecraftInstance::getStatus)
        .containsExactly(instance.label(), new Ip(instance.ip().getFirst()), instance.status());
  }

  @Test
  void nullWhenLinodeInstanceDoesNotExist() {
    LinodeInstance instance = serverApi.createInstance("Minecraft", "Minecraft");

    MinecraftInstance minecraftInstance = service.startMinecraftInstance(new LinodeId(99999L));

    assertThat(minecraftInstance).isNull();
  }
}
