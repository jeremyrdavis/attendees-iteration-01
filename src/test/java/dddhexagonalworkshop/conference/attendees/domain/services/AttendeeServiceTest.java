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
        // Given: Prepare the command to register an attendee
}
