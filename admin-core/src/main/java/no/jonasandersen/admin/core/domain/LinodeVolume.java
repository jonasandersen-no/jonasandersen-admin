package no.jonasandersen.admin.core.domain;

public record LinodeVolume(VolumeId id, String label, String status, LinodeId linodeId) {

}
