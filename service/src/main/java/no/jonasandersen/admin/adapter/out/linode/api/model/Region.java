package no.jonasandersen.admin.adapter.out.linode.api.model;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.api.model.region.Resolvers;

public record Region(String id, String label, String country, List<String> capabilities,
                     String status, Resolvers resolvers) {

}

