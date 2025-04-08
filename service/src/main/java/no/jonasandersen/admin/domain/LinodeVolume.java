package no.jonasandersen.admin.domain;

public record LinodeVolume(VolumeId id, String label, String status, LinodeId linodeId) {}
