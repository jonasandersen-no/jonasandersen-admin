package org.springframework.test.web.servlet.request;

import org.springframework.http.HttpMethod;

public class MockMvcExtensions {


  public static MockHttpServletRequestBuilder hxGet(String urlTemplate, Object... uriVariables) {
    return new MockHttpServletRequestBuilder(HttpMethod.GET, urlTemplate, uriVariables).header(
        "HX-Request", "GET");
  }
}
