package no.jonasandersen.admin.adapter;

import no.jonasandersen.admin.application.port.PrincipalNameResolver;
import org.springframework.security.core.context.SecurityContextHolder;

public class DefaultPrincipalNameResolver implements PrincipalNameResolver {

  private DefaultPrincipalNameResolver() {
  }

  public static DefaultPrincipalNameResolver create() {
    return new DefaultPrincipalNameResolver();
  }

  @Override
  public String get() {
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    return "unknown";
  }
}
