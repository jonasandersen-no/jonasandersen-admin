package no.jonasandersen.admin.adapter.out.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
class RolesConverter implements AttributeConverter<Set<RolesDbo>, String> {

  @Override
  public String convertToDatabaseColumn(Set<RolesDbo> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return "";
    }
    return attribute.stream().map(RolesDbo::name).collect(Collectors.joining(","));
  }

  @Override
  public Set<RolesDbo> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) {
      return Set.of();
    }

    return Arrays.stream(dbData.split(","))
        .map(RolesDbo::valueOf)
        .collect(Collectors.toUnmodifiableSet());
  }
}
