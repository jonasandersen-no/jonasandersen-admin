package no.jonasandersen.admin.adapter.out.linode;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class LinodeRequestInterceptor implements ClientHttpRequestInterceptor {

  private static final String AUTH_URL = "https://auth.jonasandersen.no";
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

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", "client_credentials");

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/x-www-form-urlencoded");
    HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<IdentityAccessTokenResource> resource = null;
    try {

      resource = restTemplate.exchange("/oauth2/token", HttpMethod.POST, requestEntity,
          IdentityAccessTokenResource.class);
    } catch (Exception e) {
      logger.info("Error getting access token", e);
    }
    Long expiresInSeconds = resource.getBody().getExpiresIn();
    Calendar instance = Calendar.getInstance();

    instance.add(Calendar.SECOND, expiresInSeconds.intValue());

    expires = instance.getTime();
    return resource.getBody().getAccessToken();
  }

}