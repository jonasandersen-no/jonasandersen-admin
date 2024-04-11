package no.jonasandersen.admin.core.domain;

//Volume{id=VolumeId[id=2034287], label='minecraft-volume-01', status='active', linodeId=null}
public record LinodeVolume(VolumeId id, String label, String status, LinodeId linodeId) {



}
