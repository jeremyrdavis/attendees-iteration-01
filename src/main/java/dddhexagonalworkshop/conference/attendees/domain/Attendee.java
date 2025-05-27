package dddhexagonalworkshop.conference.attendees.domain;

import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;
import dddhexagonalworkshop.conference.attendees.domain.services.AttendeeRegistrationResult;

public class Attendee {

    String email;

    public static AttendeeRegistrationResult registerAttendee(String email) {
        Attendee attendee = new Attendee();
        attendee.email = email;
        AttendeeRegisteredEvent attendeeRegisteredEvent = new AttendeeRegisteredEvent(attendee.email);

        return new AttendeeRegistrationResult(attendee, attendeeRegisteredEvent);
    }

    public String getEmail() {
        return email;
    }
}
