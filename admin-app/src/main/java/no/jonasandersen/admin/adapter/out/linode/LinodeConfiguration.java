package no.jonasandersen.admin.adapter.out.linode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class LinodeConfiguration {

  @Bean
  LinodeExchange linodeExchange() {
    RestClient client = RestClient.builder()
        .baseUrl("https://api.jonasandersen.no/linode")
        .build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(
        RestClientAdapter.create(client)).build();

    return factory.createClient(LinodeExchange.class);
  }
}
