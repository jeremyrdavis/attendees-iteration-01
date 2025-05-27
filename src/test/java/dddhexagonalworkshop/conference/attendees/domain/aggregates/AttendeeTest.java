package dddhexagonalworkshop.conference.attendees.domain.aggregates;

import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;
import dddhexagonalworkshop.conference.attendees.domain.services.AttendeeRegistrationResult;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class AttendeeTest {

    @Test
    @DisplayName("Should successfully register an attendee")
    public void testRegisterAttendee() {
        // Arrange
        String email = "john.doe@example.com";
        String firstName = "John";
        String lastName = "Doe";
        Address address = new Address(
                "123 Main St",
                "Apt 4B",
                "New York",
                "NY",
                "10001",
                "USA"
        );

        // Act
        AttendeeRegistrationResult result = Attendee.registerAttendee(email, firstName, lastName, address);
        Attendee attendee = result.attendee();
        AttendeeRegisteredEvent event = result.attendeeRegisteredEvent();

        // Assert
        assertNotNull(attendee, "Attendee should not be null");
        assertNotNull(event, "Event should not be null");
        assertEquals(email, attendee.getEmail(), "Email should match");
        assertEquals("John Doe", attendee.getFullName(), "Full name should match");
        assertEquals(address, attendee.getAddress(), "Address should match");
        assertEquals(email, event.email(), "Event email should match");
        assertEquals("John Doe", event.fullName(), "Event full name should match");
    }

    @Test
    @DisplayName("Should get correct full name")
    public void testGetFullName() {
        // Arrange
        Address address = new Address(
                "123 Main St",
                null,
                "New York",
                "NY",
                "10001",
                "USA"
        );
        Attendee attendee = new Attendee("jane.doe@example.com", "Jane", "Doe", address);

        // Act
        String fullName = attendee.getFullName();

        // Assert
        assertEquals("Jane Doe", fullName, "Full name should be correctly concatenated");
    }

    @Test
    @DisplayName("Should get correct address")
    public void testGetAddress() {
        // Arrange
        Address address = new Address(
                "123 Main St",
                "Apt 4B",
                "New York",
                "NY",
                "10001",
                "USA"
        );
        Attendee attendee = new Attendee("john.doe@example.com", "John", "Doe", address);

        // Act
        Address retrievedAddress = attendee.getAddress();

        // Assert
        assertNotNull(retrievedAddress, "Address should not be null");
        assertEquals(address, retrievedAddress, "Retrieved address should match the original");
        assertEquals("123 Main St", retrievedAddress.street(), "Street should match");
        assertEquals("Apt 4B", retrievedAddress.street2(), "Street2 should match");
        assertEquals("New York", retrievedAddress.city(), "City should match");
        assertEquals("NY", retrievedAddress.stateOrProvince(), "State should match");
        assertEquals("10001", retrievedAddress.postCode(), "Post code should match");
        assertEquals("USA", retrievedAddress.country(), "Country should match");
    }

    @Test
    @DisplayName("Should get correct email")
    public void testGetEmail() {
        // Arrange
        Address address = new Address(
                "123 Main St",
                null,
                "New York",
                "NY",
                "10001",
                "USA"
        );
        String email = "test@example.com";
        Attendee attendee = new Attendee(email, "Test", "User", address);

        // Act
        String retrievedEmail = attendee.getEmail();

        // Assert
        assertEquals(email, retrievedEmail, "Email should match");
    }
}
