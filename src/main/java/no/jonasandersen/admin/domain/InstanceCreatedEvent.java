package no.jonasandersen.admin.domain;

public record InstanceCreatedEvent(LinodeId linodeId, VolumeId volumeId, ServerType serverType) {

}
