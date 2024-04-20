package no.jonasandersen.admin.adapter.in.web;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import no.jonasandersen.admin.adapter.out.websocket.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JteHtmlGenerator {

  private static final Logger logger = LoggerFactory.getLogger(JteHtmlGenerator.class);
  private final TemplateEngine jteTemplateEngine;

  JteHtmlGenerator(TemplateEngine jteTemplateEngine) {
    this.jteTemplateEngine = jteTemplateEngine;
  }

  /**
   * Generate HTML from a JTE file. Inserting model data into the template.
   *
   * @param jteFileName The JTE file to render. Excluding the file extension.
   * @param model       The model to insert into the template.
   * @return The rendered HTML.
   */
  public String generateHtml(String jteFileName, Model model) {
    String fileName = jteFileName + ".jte";
    logger.info("Generating HTML from JTE file: {} with model {}", fileName,
        model.model());
    StringOutput stringOutput = new StringOutput();
    jteTemplateEngine.render(fileName, model.model(), stringOutput);
    return stringOutput.toString();
  }

  public String generateHtml(String jteFileName) {
    StringOutput stringOutput = new StringOutput();
    logger.info("Generating HTML from JTE file: {}", jteFileName + ".jte");
    jteTemplateEngine.render(jteFileName + ".jte", Model.empty(), stringOutput);
    return stringOutput.toString();
  }
}
