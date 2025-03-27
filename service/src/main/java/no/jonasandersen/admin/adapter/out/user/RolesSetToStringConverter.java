package no.jonasandersen.admin.adapter.out.user;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;

class RolesSetToStringConverter implements Converter<Set<RolesDbo>, String> {

  @Override
  public String convert(Set<RolesDbo> source) {
    if (source.isEmpty()) {
      return "";
    }
    return source.stream().map(RolesDbo::name).collect(Collectors.joining(","));
  }
}
