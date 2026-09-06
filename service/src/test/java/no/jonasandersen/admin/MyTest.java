package no.jonasandersen.admin;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyTest {

  @Test
  void name() {
    System.out.println(new BCryptPasswordEncoder().encode("admin"));
  }
}
