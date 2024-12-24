package no.jonasandersen.admin.adapter.out.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;

class RolesConverterTest {

  private final RolesConverter converter = new RolesConverter();

  @Test
  void convertToDatabaseColumn() {
    String result = converter.convertToDatabaseColumn(Set.of(RolesDbo.USER, RolesDbo.ADMIN));

    assertThat(result).contains("ADMIN", "USER");
  }

  @Test
  void singleRolesDoesNotContainComma() {
    String result = converter.convertToDatabaseColumn(Set.of(RolesDbo.USER));

    assertThat(result).isEqualTo("USER");
  }

  @Test
  void convertToListOfEnum() {
    Set<RolesDbo> result = converter.convertToEntityAttribute("USER,ADMIN");

    assertThat(result).containsExactlyInAnyOrder(RolesDbo.USER, RolesDbo.ADMIN);
  }
}