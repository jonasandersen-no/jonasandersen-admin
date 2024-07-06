package no.jonasandersen.admin.adapter.out.linode.api.model;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.LinodeVolumeDto;

public record Page<T>(List<T> data, Integer page, Integer pages, Integer results) {

  public static Page<LinodeVolumeDto> empty() {
    return new Page<>(List.of(), 0, 0, 0);
  }
}
