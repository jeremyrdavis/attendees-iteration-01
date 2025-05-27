# Workshop Workflow

## Iteration 02: Add Attendee's Address

### Overview
In this iteration we will enhance the `Attendee` model by adding an address field. This will allow us to store more detailed information about each attendee.

### Steps

#### 1. Create a Value Object to represent the Attendee's Address

Create a record, Address, in the domain/valueobjects package. This will be used to represent the address of an attendee.

```java
public record Address(
        String street,
        String street2,
        String city,
        String stateOrProvince,
        String postCode,
        String country
) {
}
```

Optional: Add validation to the Address record to ensure that all fields are properly formatted and not empty.

```java
/**
 * Address value object that encapsulates address validation and behavior.
 * This is an example of a Domain-Driven Design value object implemented as a record.
 */
public record Address(
        String street,
        String street2,
        String city,
        String stateOrProvince,
        String postCode,
        String country
) {
    /**
     * Compact constructor for validation
     */
    public Address {
        validate(street, city, stateOrProvince, postCode, country);
    }

    /**
     * Validates that the address components are properly formatted.
     *
     * @throws IllegalArgumentException if the address is invalid
     */
    private void validate(String street, String city, String stateOrProvince, String postCode, String country) {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be empty");
        }

        if (stateOrProvince == null || stateOrProvince.isBlank()) {
            throw new IllegalArgumentException("State or province cannot be empty");
        }

        if (postCode == null || postCode.isBlank()) {
            throw new IllegalArgumentException("Postal code cannot be empty");
        }

        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }

        if (street.length() > 100) {
            throw new IllegalArgumentException("Street is too long (max 100 characters)");
        }

        if (city.length() > 50) {
            throw new IllegalArgumentException("City is too long (max 50 characters)");
        }

        if (stateOrProvince.length() > 50) {
            throw new IllegalArgumentException("State or province is too long (max 50 characters)");
        }

        if (postCode.length() > 20) {
            throw new IllegalArgumentException("Postal code is too long (max 20 characters)");
        }

        if (country.length() > 50) {
            throw new IllegalArgumentException("Country is too long (max 50 characters)");
        }
    }

    /**
     * Returns a formatted single-line address string.
     *
     * @return formatted address
     */
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder(street);
        if (street2 != null && !street2.isBlank()) {
            sb.append(", ").append(street2);
        }
        sb.append(", ").append(city)
                .append(", ").append(stateOrProvince)
                .append(" ").append(postCode)
                .append(", ").append(country);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormattedAddress();
    }
}
```

#### 2. Add the Address field and First and Last Name fields to the RegisterAttendeeCommand and Attendee aggregate model

First, update the `RegisterAttendeeCommand` to include the new address field along with first and last name fields.
```java
public record RegisterAttendeeCommand(String email, String firstName, String lastName, Address address) {
}
```

Second, update the `Attendee` aggregate model to include the new address field and first and last name fields.

NOTE: We are creating a method, "getFullName", to return the full name of the attendee by concatenating the first and last names.

```java
package dddhexagonalworkshop.conference.attendees.domain;

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

    public AttendeeRegistrationResult registerAttendee(String email, String firstName, String lastName, Address address) {
        // Here you would typically perform some business logic, like checking if the attendee already exists
        // and then create an event to publish.
        AttendeeRegisteredEvent event = new AttendeeRegisteredEvent(email, this.getFullName());
        return new AttendeeRegistrationResult(this, event);
    }

    public String getEmail() {
        return email;
    }

    String getFullName() {
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
```

#### 3. Update the `AttendeeRegisteredEvent` to include the new address field and the full name of the Attenddee.

```java
package dddhexagonalworkshop.conference.attendees.domain.events;

public record AttendeeRegisteredEvent(String email, String fullName) {
}
```

#### 4. Update the persistence layer to handle the new fields

First, add the new fields to the `AttendeeEntity` class in the persistence layer.

```java
package dddhexagonalworkshop.conference.attendees.persistence;

import jakarta.persistence.*;

@Entity @Table(name = "attendee")
public class AttendeeEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    AddressEntity address;

    private String email;

    protected AttendeeEntity() {
    }

    protected AttendeeEntity(String email, AddressEntity address) {
        this.email = email;
        this.address = address;
    }

    protected Long getId() {
        return id;
    }

    protected String getEmail() {
        return email;
    }

}
```

Second, create a new `AddressEntity` class to represent the address in the persistence layer.

```java
package dddhexagonalworkshop.conference.attendees.persistence;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class AddressEntity {

    @Id @GeneratedValue
    private Long id;

    String street;

    String street2;

    String city;

    String stateOrProvince;

    String postCode;

    String country;

    protected AddressEntity() {
    }

    protected AddressEntity(String street, String street2, String city, String stateOrProvince, String postCode, String country) {
        this.street = street;
        this.street2 = street2;
        this.city = city;
        this.stateOrProvince = stateOrProvince;
        this.postCode = postCode;
        this.country = country;
    }

    String getStreet() {
        return street;
    }

    void setStreet(String street) {
        this.street = street;
    }

    String getStreet2() {
        return street2;
    }

    void setStreet2(String street2) {
        this.street2 = street2;
    }

    String getCity() {
        return city;
    }

    void setCity(String city) {
        this.city = city;
    }

    String getStateOrProvince() {
        return stateOrProvince;
    }

    void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    String getPostCode() {
        return postCode;
    }

    void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    String getCountry() {
        return country;
    }

    void setCountry(String country) {
        this.country = country;
    }
}

```

Third, update the `AttendeeRepository` to handle the new fields:

```java
package dddhexagonalworkshop.conference.attendees.persistence;

import dddhexagonalworkshop.conference.attendees.domain.Attendee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

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

#### 5. Update the Service to handle the new fields

First update the `AttendeeDTO` that is used to transfer data between the service and the controller to include the new address field.
```java
package dddhexagonalworkshop.conference.attendees.infrastrcture;

import dddhexagonalworkshop.conference.attendees.domain.valueobjects.Address;

public record AttendeeDTO(String email, Address addressEntity) {
}
```

Second, update the `AttendeeService` to handle the new fields:

```java
package dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.domain.Attendee;
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
```
## Summary
In this second iteration, we created a Value Object to hold the Attendee's address and updated the rest of the application.

### Key points
***Value Objects***: 