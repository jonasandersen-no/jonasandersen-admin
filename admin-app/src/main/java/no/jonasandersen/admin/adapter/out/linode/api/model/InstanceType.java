package no.jonasandersen.admin.adapter.out.linode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.api.model.type.Addons;
import no.jonasandersen.admin.adapter.out.linode.api.model.type.Price;
import no.jonasandersen.admin.adapter.out.linode.api.model.type.RegionPrice;

public record InstanceType(String id, String label, Price price,
                           @JsonProperty("region_prices") List<RegionPrice> regionPrices,
                           Addons addons, int memory, int disk, int transfer, int vcpus, int gpus,
                           @JsonProperty("network_out") int networkOut,
                           @JsonProperty("class") String clazz, String successor) {

}

