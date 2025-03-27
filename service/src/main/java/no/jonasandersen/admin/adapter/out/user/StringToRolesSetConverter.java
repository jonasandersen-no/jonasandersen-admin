package no.jonasandersen.admin.adapter.out.user;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;

class StringToRolesSetConverter implements Converter<String, Set<RolesDbo>> {

  @Override
  public Set<RolesDbo> convert(String source) {
    if (source.isBlank()) {
      return Set.of();
    }
    return Arrays.stream(source.split(","))
        .map(RolesDbo::valueOf)
        .collect(Collectors.toUnmodifiableSet());
  }
}
