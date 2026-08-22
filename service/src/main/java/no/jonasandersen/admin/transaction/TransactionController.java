package no.jonasandersen.admin.transaction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Controller
public class TransactionController {

  private final JsonMapper jsonMapper;
  private static final String BASE_URL = "https://money.jonasandersen.dev";
  private static final String PERSONAL_ACCESS_TOKEN =
      "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIwMWEwMDU3MS1iMzhkLTcyMDItOWFiMi0yZjZmYWI0YTI0MTciLCJqdGkiOiIzNmY3NDQ3Mzc4NDlkYmNjNDZhNWYzNTRjMjk3YzIxNzM3OGY4NjlmNjdlMWFkZjE1MzQwYjE3ODBkYzA1NDAwODFhZDQ3NzEwNmE4ZGM2YyIsImlhdCI6MTc4Njg4OTcwOS44MzY1MzksIm5iZiI6MTc4Njg4OTcwOS44MzY1NDMsImV4cCI6MTgxODQyNTcwOS43MTE4OTUsInN1YiI6IjEiLCJzY29wZXMiOltdfQ.U0ZRhuBNzODSfoczdMeKXU-OSsLZ3g4ki4l-vt2stHTPpvkm7mVx96syrVDRaSr-UJ98QaGXQv-Wzdvmg4jZ75NnV8etwhqCPIFTv78ibWlJKFUR-8EY8LOv57ZhNxJyiYvQay2ZTOcyZg1xEMpbobq2UXfrUYFVggMypjdbwF3Y-GaNSfYNpspzamJ6iNXTPNgd_5xWm71bPw1eNqMq2CBKm1HR2Rejk4JPING4VeprNT_yPtBWnVbMQ9USnamqN5i_jNsR_JHSdPr9vWxNRZFymruKBdamhxBr1M4a-Ln_ZlMW-HAY9YeUOfwM7ujDTG5PyIWYFuG8XNI6y49o26WA3hfur8Zifsix9N3YDpXLgojmZG06NbsCbcDjL6_AzG48565OswNDC35du0AhnxjS2fYnun35RIKuFmaaAaz1Sd26aEJUXG553X1DC5v36HGnQzsJg_w-eChrdbGymqPpKlRPi9zbpctVKy6UjLYLXC7E3LT2qYF2mF1g335zQWh7Vknpc5VetgbKiPTe8pPEd-g8ef_mxFj_3gj-a2QLYQ7hlC0lSAHQHFYlh3pFywIm-sdGjyEuNvx1-_XltFXS7-ycrZ3TETRA9O-sK2UTxDFJQ79WVGjCf_pr_X14e62cixfC31ngdNcoMnANLkEMCLoHVzAd6HtSu6wyk6c";

  private final HttpClient client = HttpClient.newHttpClient();

  public TransactionController(JsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @GetMapping("/transactions")
  public String transactions() {
    return "transactions/index";
  }

  @GetMapping(value = "/accounts", produces = "text/html")
  @ResponseBody
  public String getAccounts() {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/v1/accounts?type=asset"))
            .header("Authorization", "Bearer " + PERSONAL_ACCESS_TOKEN)
            .header("Accept", "application/json")
            .GET()
            .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      JsonNode root = jsonMapper.readTree(response.body());

      StringBuilder optionsHtml = new StringBuilder();
      for (JsonNode account : root.path("data")) {
        String name = account.path("attributes").path("name").asText();
        optionsHtml.append(String.format("<option value=\"%s\">%s</option>", name, name));
      }
      return optionsHtml.toString();
    } catch (Exception e) {
      return "<option value=\"\">Failed to load accounts</option>";
    }
  }

  @PostMapping(value = "/transactions", produces = "text/html")
  @ResponseBody
  public String createTransaction(
      @RequestParam String description,
      @RequestParam String amount,
      @RequestParam String sourceName,
      @RequestParam String destinationName) {

    String jsonPayload =
        """
            {
              "apply_rules": true,
              "transactions": [
                {
                  "type": "withdrawal",
                  "date": "%s",
                  "amount": "%s",
                  "description": "%s",
                  "source_name": "%s",
                  "destination_name": "%s"
                }
              ]
            }
            """
            .formatted(LocalDate.now(), amount, description, sourceName, destinationName);

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/v1/transactions"))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + PERSONAL_ACCESS_TOKEN)
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200 || response.statusCode() == 201) {
        return "<div class='result success'>Transaction created successfully!</div>";
      } else {
        return "<div class='result error'>Error: Firefly returned status code "
               + response.statusCode()
               + "</div>";
      }
    } catch (Exception e) {
      return "<div class='result error'>Failed to reach Firefly III: " + e.getMessage() + "</div>";
    }
  }
}
