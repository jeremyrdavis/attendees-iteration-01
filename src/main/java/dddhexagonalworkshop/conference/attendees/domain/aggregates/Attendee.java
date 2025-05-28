package dddhexagonalworkshop.conference.attendees.domain.aggregates;

import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;
import dddhexagonalworkshop.conference.attendees.domain.services.AttendeeRegistrationResult;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.Address;

public class Attendee {

    String email;

    String firstName;

    String lastName;

    Address address;

    public Attendee(String email, String firstName, String lastName, Address address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public static AttendeeRegistrationResult registerAttendee(String email, String firstName, String lastName, Address address) {
        // Here you would typically perform some business logic, like checking if the attendee already exists
        // and then create an event to publish.
        Attendee attendee = new Attendee(email, firstName, lastName, address);
        AttendeeRegisteredEvent event = new AttendeeRegisteredEvent(email, attendee.getFullName());
        return new AttendeeRegistrationResult(attendee, event);
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    public Address getAddress() {
        return address;
    }
}
