package no.jonasandersen.admin.adapter.out.linode;

public record LinodeVolumeDto(Long id, String label, String status, Long linodeId) {}
