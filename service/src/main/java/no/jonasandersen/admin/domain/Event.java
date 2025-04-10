package no.jonasandersen.admin.domain;

public sealed interface Event permits TestEvent, TestEvent2 {}
