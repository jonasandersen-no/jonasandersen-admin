package no.jonasandersen.admin;

import java.util.List;
import no.jonasandersen.admin.generated.api.MeApi;
import no.jonasandersen.admin.generated.model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CurrentUserController implements MeApi {

  @Override
  public ResponseEntity<UserResponse> me() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(401).build();
    }

    List<String> roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    return ResponseEntity.ok(new UserResponse(authentication.getName(), roles));
  }

}