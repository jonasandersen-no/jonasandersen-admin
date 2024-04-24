package no.jonasandersen.admin.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JteHtmlGeneratorTest extends IoBasedTest {

  @Autowired
  private JteHtmlGenerator generator;

  //language=html
  @Test
  void generateJteTemplate() {
    String renderedHtml = generator.generateHtml("test");

    assertThat(renderedHtml)
        .isEqualTo("""
            <div>
                <h1>Testing</h1>
            </div>""");
  }
}