package no.jonasandersen.admin.adapter.out.linode.api.model;

import java.util.List;

public record Page<T>(List<T> data, Integer page, Integer pages, Integer results) {

}
