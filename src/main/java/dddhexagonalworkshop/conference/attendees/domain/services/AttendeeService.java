package dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.domain.aggregates.Attendee;
import dddhexagonalworkshop.conference.attendees.infrastrcture.AttendeeDTO;
import dddhexagonalworkshop.conference.attendees.infrastrcture.AttendeeEventPublisher;
import dddhexagonalworkshop.conference.attendees.persistence.AttendeeRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AttendeeService {

    @Inject
    AttendeeRepository attendeeRepository;

    @Inject
    AttendeeEventPublisher attendeeEventPublisher;

    @Transactional
    public AttendeeDTO registerAttendee(RegisterAttendeeCommand registerAttendeeAttendeeCommand) {
        // Logic to register an attendee
        AttendeeRegistrationResult result = Attendee.registerAttendee(registerAttendeeAttendeeCommand.email(),
                registerAttendeeAttendeeCommand.firstName(),
                registerAttendeeAttendeeCommand.lastName(),
                registerAttendeeAttendeeCommand.address());


        //persist the attendee
        QuarkusTransaction.requiringNew().run(() -> {
            attendeeRepository.persist(result.attendee());
        });

        //notify the system that a new attendee has been registered
        attendeeEventPublisher.publish(result.attendeeRegisteredEvent());

        return new AttendeeDTO(result.attendee().getEmail(), result.attendee().getFullName());
    }
}
