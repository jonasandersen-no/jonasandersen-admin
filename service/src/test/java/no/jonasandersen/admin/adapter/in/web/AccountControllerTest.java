package no.jonasandersen.admin.adapter.in.web;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountControllerTest {

  @Test
  void test() {
    AccountController controller = new AccountController();

    System.out.println(controller.accounts());
  }
}
