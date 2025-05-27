package dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.domain.Attendee;
import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;

public record AttendeeRegistrationResult(Attendee attendee, AttendeeRegisteredEvent attendeeRegisteredEvent) {
}
