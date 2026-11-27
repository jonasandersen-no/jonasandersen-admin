package no.jonasandersen.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	private final VueIndexHtmlController indexController;

  public MvcConfig(VueIndexHtmlController indexController) {
    this.indexController = indexController;
  }

  @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("*", "**")
                .addResourceLocations("classpath:/static/")
                .setUseLastModified(true)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new ChainedPathResourceResolver())
                .addResolver(new VueResourceResolver(indexController));
    }
}
