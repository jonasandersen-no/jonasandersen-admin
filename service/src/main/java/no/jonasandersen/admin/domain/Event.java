package no.jonasandersen.admin.domain;

public sealed interface Event permits SaveFileEvent, TestEvent, TestEvent2 {}
