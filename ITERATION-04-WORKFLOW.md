# Workshop Workflow

## Iteration 04: Testing

### Overview
In this iteration we will add Unit and Integration tests.

### Steps

#### 1. Create a Unit Test for the Attendee aggregate

One of the advantages of encapsulating the business logic in an aggregate is that we can test it independently of the rest of the system. Create a unit test for the `Attendee` aggregate.

````java
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
        String email = "frodo.baggins@shire.me";
        String firstName = "Frodo";
        String lastName = "Baggins";
        Address address = new Address(
                "Bag End",
                "Bagshot Row",
                "Hobbiton",
                "The Shire",
                "SH1R3",
                "Middle Earth"
        );

        // Act
        AttendeeRegistrationResult result = Attendee.registerAttendee(email, firstName, lastName, address);
        Attendee attendee = result.attendee();
        AttendeeRegisteredEvent event = result.attendeeRegisteredEvent();

        // Assert
        assertNotNull(attendee, "Attendee should not be null");
        assertNotNull(event, "Event should not be null");
        assertEquals(email, attendee.getEmail(), "Email should match");
        assertEquals("Frodo Baggins", attendee.getFullName(), "Full name should match");
        assertEquals(address, attendee.getAddress(), "Address should match");
        assertEquals(email, event.email(), "Event email should match");
        assertEquals("Frodo Baggins", event.fullName(), "Event full name should match");
    }

    @Test
    @DisplayName("Should get correct full name")
    public void testGetFullName() {
        // Arrange
        Address address = new Address(
                "Meduseld",
                null,
                "Edoras",
                "Rohan",
                "RH4N",
                "Middle Earth"
        );
        Attendee attendee = new Attendee("eowyn@rohan.me", "Éowyn", "of Rohan", address);

        // Act
        String fullName = attendee.getFullName();

        // Assert
        assertEquals("Éowyn of Rohan", fullName, "Full name should be correctly concatenated");
    }

    @Test
    @DisplayName("Should get correct address")
    public void testGetAddress() {
        // Arrange
        Address address = new Address(
                "Citadel",
                "7th Level",
                "Minas Tirith",
                "Gondor",
                "MT777",
                "Middle Earth"
        );
        Attendee attendee = new Attendee("aragorn@gondor.me", "Aragorn", "Elessar", address);

        // Act
        Address retrievedAddress = attendee.getAddress();

        // Assert
        assertNotNull(retrievedAddress, "Address should not be null");
        assertEquals(address, retrievedAddress, "Retrieved address should match the original");
        assertEquals("Citadel", retrievedAddress.street(), "Street should match");
        assertEquals("7th Level", retrievedAddress.street2(), "Street2 should match");
        assertEquals("Minas Tirith", retrievedAddress.city(), "City should match");
        assertEquals("Gondor", retrievedAddress.stateOrProvince(), "State should match");
        assertEquals("MT777", retrievedAddress.postCode(), "Post code should match");
        assertEquals("Middle Earth", retrievedAddress.country(), "Country should match");
    }

    @Test
    @DisplayName("Should get correct email")
    public void testGetEmail() {
        // Arrange
        Address address = new Address(
                "Grey Havens",
                null,
                "Lindon",
                "Eriador",
                "GH123",
                "Middle Earth"
        );
        String email = "gandalf@istari.me";
        Attendee attendee = new Attendee(email, "Gandalf", "the Grey", address);

        // Act
        String retrievedEmail = attendee.getEmail();

        // Assert
        assertEquals(email, retrievedEmail, "Email should match");
    }
    
    @Test
    @DisplayName("Should verify AttendeeRegisteredEvent contains correct data")
    public void testAttendeeRegisteredEvent() {
        // Arrange
        String email = "legolas@mirkwood.me";
        String firstName = "Legolas";
        String lastName = "Greenleaf";
        Address address = new Address(
                "Royal Palace",
                "Woodland Realm",
                "Mirkwood",
                "Rhovanion",
                "MK001",
                "Middle Earth"
        );
        
        // Act
        AttendeeRegistrationResult result = Attendee.registerAttendee(email, firstName, lastName, address);
        AttendeeRegisteredEvent event = result.attendeeRegisteredEvent();
        
        // Assert
        assertNotNull(event, "Event should not be null");
        assertEquals(email, event.email(), "Event email should match");
        assertEquals("Legolas Greenleaf", event.fullName(), "Event full name should match");
    }
}
````
#### 2. Create an Integration Test for the AttendeeService

```java
package dddhexagonalworkshop.conference.attendees.persistence;

import dddhexagonalworkshop.conference.attendees.domain.aggregates.Attendee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AttendeeRepository implements PanacheRepository<AttendeeEntity> {


    public void persist(Attendee aggregate) {
        // transform the aggregate to an entity
        AttendeeEntity attendeeEntity = fromAggregate(aggregate);
        persist(attendeeEntity);
    }

    private AttendeeEntity fromAggregate(Attendee attendee) {
        AddressEntity addressEntity = new AddressEntity(
                attendee.getAddress().street(),
                attendee.getAddress().street2(),
                attendee.getAddress().city(),
                attendee.getAddress().stateOrProvince(),
                attendee.getAddress().postCode(),
                attendee.getAddress().country()
        );
        AttendeeEntity entity = new AttendeeEntity(attendee.getEmail(), addressEntity);
        return entity;
    }
}
```
