package no.jonasandersen.admin.domain;

public sealed interface Event permits HabitEvent, SaveFileEvent, TestEvent, TestEvent2 {}
