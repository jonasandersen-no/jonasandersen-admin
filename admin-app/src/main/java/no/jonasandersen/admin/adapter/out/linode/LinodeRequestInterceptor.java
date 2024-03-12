package no.jonasandersen.admin.adapter.out.linode;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LinodeRequestInterceptor implements ClientHttpRequestInterceptor {

  private static final String AUTH_URL = "https://login.bjoggis.com";
  private static final Logger logger = LoggerFactory.getLogger(LinodeRequestInterceptor.class);

  @Value("${spring.security.oauth2.client.registration.spring.client-id:}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.spring.client-secret:}")
  private String clientSecret;

  private Date expires;
  private String accessToken;

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    if (expires == null || expires.before(new Date())) {
      accessToken = getAccessToken();
    }
    request.getHeaders().add("Authorization", "Bearer " + accessToken);
    return execution.execute(request, body);
  }

  private String getAccessToken() {
    logger.info("Getting access token");
    RestTemplate restTemplate = new RestTemplateBuilder()
        .basicAuthentication(clientId, clientSecret)
        .rootUri(AUTH_URL)
        .build();

    IdentityAccessTokenResource resource = null;
    try {
      resource = restTemplate.postForObject(
          "/oauth2/token?grant_type=client_credentials", null, IdentityAccessTokenResource.class);
    } catch (Exception e) {
      logger.info("Error getting access token", e);
    }
    Long expiresInSeconds = resource.getExpiresIn();
    Calendar instance = Calendar.getInstance();

    instance.add(Calendar.SECOND, expiresInSeconds.intValue());

    expires = instance.getTime();
    return resource.getAccessToken();
  }

}