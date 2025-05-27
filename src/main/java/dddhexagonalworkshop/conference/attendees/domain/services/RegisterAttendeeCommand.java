package dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.domain.valueobjects.Address;

public record RegisterAttendeeCommand(String email, String firstName, String lastName, Address address) {
}
