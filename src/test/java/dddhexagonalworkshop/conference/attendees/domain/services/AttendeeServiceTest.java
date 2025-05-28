package dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.domain.aggregates.Attendee;
import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;
import dddhexagonalworkshop.conference.attendees.infrastrcture.AttendeeEventPublisher;
import dddhexagonalworkshop.conference.attendees.persistence.AttendeeEntity;
import dddhexagonalworkshop.conference.attendees.persistence.AttendeeRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class AttendeeServiceTest {

    @Inject
    AttendeeService attendeeService;

    @InjectMock
    AttendeeRepository attendeeRepository;

    @InjectMock
    AttendeeEventPublisher attendeeEventPublisher;

    @BeforeEach
    public void setUp() {
        // Setup code if needed, e.g., mocking behavior of injected components
        Mockito.doNothing().when(attendeeRepository).persist(any(AttendeeEntity.class));
        Mockito.doNothing().when(attendeeEventPublisher).publish(any(AttendeeRegisteredEvent.class));
    }

    @Test
    public void testRegisterAttendee() {
        // Given: Prepare the command to register an attendee with Lord of the Rings data
        RegisterAttendeeCommand command = new RegisterAttendeeCommand(
                "frodo.baggins@shire.me",
                "Frodo",
                "Baggins",
                new dddhexagonalworkshop.conference.attendees.domain.valueobjects.Address(
                        "Bag End",
                        "Bagshot Row",
                        "Hobbiton",
                        "The Shire",
                        "SH1 1RE",
                        "Middle Earth"
                ),
                dddhexagonalworkshop.conference.attendees.domain.valueobjects.MealPreference.VEGETARIAN,
                dddhexagonalworkshop.conference.attendees.domain.valueobjects.TShirtSize.S
        );

        // When: Call the service method
        attendeeService.registerAttendee(command);

        // Then: Verify the repository and event publisher were called
        Mockito.verify(attendeeRepository).persist(any(
                dddhexagonalworkshop.conference.attendees.domain.aggregates.Attendee.class));
        Mockito.verify(attendeeEventPublisher).publish(any(
                dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent.class));
    }
}
