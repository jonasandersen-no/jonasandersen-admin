package no.jonasandersen.admin.adapter.out.linode.api.model.instance;

public record Alerts(int cpu, int networkIn, int networkOut, int transferQuota, int io) {}
