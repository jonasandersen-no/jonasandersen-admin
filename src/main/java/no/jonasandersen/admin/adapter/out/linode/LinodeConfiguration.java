package no.jonasandersen.admin.adapter.out.linode;

import no.jonasandersen.admin.AdminProperties;
import no.jonasandersen.admin.AdminProperties.Linode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class LinodeConfiguration {

  @Bean
  LinodeBjoggisExchange linodeBjoggisExchange(
      LinodeBjoggisRequestInterceptor linodeBjoggisRequestInterceptor) {
    RestClient client = RestClient.builder()
        .baseUrl("https://api.jonasandersen.no/linode")
        .requestInterceptor(linodeBjoggisRequestInterceptor)
        .build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(
        RestClientAdapter.create(client)).build();

    return factory.createClient(LinodeBjoggisExchange.class);
  }

  @Bean
  LinodeExchange linodeExchange(AdminProperties properties) {
    Linode linode = properties.linode();
    RestClient restClient = RestClient.builder()
        .baseUrl(linode.baseUrl())
        .requestInitializer(request -> request.getHeaders().setBearerAuth(linode.token()))
        .build();

    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

    return factory.createClient(LinodeExchange.class);
  }

}
