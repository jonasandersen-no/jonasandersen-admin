package no.jonasandersen.admin.adapter.out.linode.api.model.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Backups(Price price, @JsonProperty("region_prices") List<RegionPrice> regionPrices) {

}
