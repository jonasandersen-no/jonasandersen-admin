package no.jonasandersen.admin.domain;

import java.util.Set;

public record User(Username username, Set<Roles> roles) {

}
