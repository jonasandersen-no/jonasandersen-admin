package no.jonasandersen.admin.core.domain;

import java.time.LocalDateTime;

public record LinodeStoredInfo(Long id, Long linodeId, String createdBy, LocalDateTime createdDate, String serverType,
                               String subDomain) {

}