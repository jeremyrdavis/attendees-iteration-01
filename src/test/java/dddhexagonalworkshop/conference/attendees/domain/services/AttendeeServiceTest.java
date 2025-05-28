package dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.infrastrcture.AttendeeEventPublisher;
import dddhexagonalworkshop.conference.attendees.persistence.AttendeeRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        org.mockito.Mockito.verify(attendeeRepository).persist(org.mockito.ArgumentMatchers.any(
                dddhexagonalworkshop.conference.attendees.domain.aggregates.Attendee.class));
        org.mockito.Mockito.verify(attendeeEventPublisher).publish(org.mockito.ArgumentMatchers.any(
                dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent.class));
    }
}
