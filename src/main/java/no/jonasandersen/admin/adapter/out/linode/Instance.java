package no.jonasandersen.admin.adapter.out.linode;

import java.util.Date;

public record Instance(Long id, String label, String ip, String status, Date created) {

}
