package no.jonasandersen.admin.adapter.out.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import no.jonasandersen.admin.domain.AccountCreatedEvent;
import no.jonasandersen.admin.domain.AccountId;
import no.jonasandersen.admin.domain.Event;
import no.jonasandersen.admin.domain.ExpenseLoggedEvent;
import no.jonasandersen.admin.domain.HabitCompletedEvent;
import no.jonasandersen.admin.domain.HabitCreatedEvent;
import no.jonasandersen.admin.domain.SaveFileCreatedEvent;
import no.jonasandersen.admin.domain.SaveFileId;
import no.jonasandersen.admin.domain.TestEvent;
import no.jonasandersen.admin.domain.TestEvent2;
import no.jonasandersen.admin.domain.TestId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EventDtoTest {

  @Test
  void allEventsAreTested() {
    Set<String> allEventClasses = findAllConcreteEventClasses();

    Set<String> eventClassesCoveredByParameterizedTest =
        events()
            .map(arg -> arg.get()[0].getClass())
            .map(Class::getSimpleName)
            .collect(Collectors.toSet());

    assertThat(eventClassesCoveredByParameterizedTest)
        .as("Missing some Events from the parameterized test")
        .containsAll(allEventClasses);
  }

  private Set<String> findAllConcreteEventClasses() {
    return findAllConcreteImplementations(Event.class).stream()
        .map(Class::getSimpleName)
        .collect(Collectors.toSet());
  }

  private Set<Class<?>> findAllConcreteImplementations(Class<?> sealedInterface) {
    Set<Class<?>> result = new HashSet<>();

    if (!sealedInterface.isInterface() && !Modifier.isAbstract(sealedInterface.getModifiers())) {
      // This is a concrete class, add it
      result.add(sealedInterface);
      return result;
    }

    // Get direct permitted subclasses/interfaces
    Class<?>[] permittedSubclasses = sealedInterface.getPermittedSubclasses();
    if (permittedSubclasses == null) {
      return result;
    }

    // Recursively process each permitted subclass/interface
    for (Class<?> subclass : permittedSubclasses) {
      if (!subclass.isInterface() && !Modifier.isAbstract(subclass.getModifiers())) {
        // This is a concrete class, add it
        result.add(subclass);
      } else {
        // This is an interface or abstract class, recurse into it
        result.addAll(findAllConcreteImplementations(subclass));
      }
    }

    return result;
  }

  @ParameterizedTest
  @MethodSource("events")
  void eventRoundTripConversion(Event sourceEvent) {
    EventDto<Event> eventDto = EventDto.from(UUID.randomUUID(), 14, sourceEvent);

    Event actual = eventDto.toDomain();

    assertThat(actual).isEqualTo(sourceEvent);
  }

  public static Stream<Arguments> events() {
    return Stream.concat(
        Stream.concat(
            Stream.of(
                Arguments.of(new TestEvent(new TestId(UUID.randomUUID()))),
                Arguments.of(new TestEvent2(new TestId(UUID.randomUUID()))),
                Arguments.of(
                    new SaveFileCreatedEvent(
                        new SaveFileId(UUID.randomUUID()), "save file name", "owner"))),
            habitEvents()),
        accountEvents());
  }

  public static Stream<Arguments> habitEvents() {
    return Stream.of(
        Arguments.of(new HabitCreatedEvent(UUID.randomUUID(), 1, "name", "goal")),
        Arguments.of(
            new HabitCompletedEvent(UUID.randomUUID(), 1, LocalDateTime.now(ZoneId.of("UTC")))));
  }

  public static Stream<Arguments> accountEvents() {
    return Stream.of(Arguments.of(new AccountCreatedEvent(AccountId.random(), "accountname")),
        Arguments.of(new ExpenseLoggedEvent(AccountId.random())));
  }
}
