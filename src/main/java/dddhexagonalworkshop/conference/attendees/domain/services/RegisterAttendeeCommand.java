package dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.domain.valueobjects.Address;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.MealPreference;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.TShirtSize;

public record RegisterAttendeeCommand(String email, String firstName, String lastName, Address address, MealPreference mealPreference, TShirtSize tShirtSize) {

    public RegisterAttendeeCommand(String email, String firstName, String lastName, Address address, MealPreference mealPreference, TShirtSize tShirtSize) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.mealPreference = mealPreference;
        this.tShirtSize = tShirtSize;
    }
}
