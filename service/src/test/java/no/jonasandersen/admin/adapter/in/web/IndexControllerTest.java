package no.jonasandersen.admin.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

class IndexControllerTest {

  private final MockMvcTester mvc = MockMvcTester.of(IndexController.class);

  @Test
  void indexPageReturnsStatus200() {
    mvc.get().uri("/")
        .assertThat()
        .hasStatus(HttpStatus.OK)
        .model()
        .extracting("attribute")
        .isEqualTo("value");
  }

  @Test
  void swaggerUiReturnsStatus302() {
    mvc.get().uri("/swagger-ui")
        .assertThat()
        .hasStatus(HttpStatus.FOUND);
  }
}