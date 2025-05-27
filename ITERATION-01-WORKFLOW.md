# Workshop Workflow

## Iteration 02: Add Attendee's Address

### Overview

First iteration: create the basic structure of the Attendee registration microservice using Hexagonal Architecture (Ports and Adapters) and Domain-Driven Design (DDD) principles.
Second iteration: add the Attendee's Address
Third iteration: add the Attendee's Payment information, calling the Payment microservice, illustrating the Conformist integration pattern, and the Money Value Object.
Fourth iteration: add the Anti-corruption Layer to illustrate the anti-corruption style integration pattern.

## First Iteration

- Create a RegisterAttendeeCommand object with a single String, "email"

```java
package dddhexagonalworkshop.conference.attendees.domain.services;

public record RegisterAttendeeCommand(String email) {
}

```

- Create the AttendeeEndpoint in the attendees/domain/services package
    - a single POST method that takes a RegisterAttendeeCommand

```java
package dddhexagonalworkshop.conference.attendees.infrastructure;

import dddhexagonalworkshop.conference.attendees.api.AttendeeDTO;
import dddhexagonalworkshop.conference.attendees.dddhexagonalworkshop.conference.attendees.domain.services.AttendeeService;
import dddhexagonalworkshop.conference.attendees.dddhexagonalworkshop.conference.attendees.domain.services.RegisterAttendeeCommand;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/attendees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendeeEndpoint {

  @Inject
  AttendeeService attendeeService;

  @POST
  public Response registerAttendee(RegisterAttendeeCommand registerAttendeeCommand) {
    Log.debugf("Creating attendee %s", registerAttendeeCommand);

    AttendeeDTO attendeeDTO = attendeeService.registerAttendee(registerAttendeeCommand);

    Log.debugf("Created attendee %s", attendeeDTO);

    return Response.created(URI.create("/" + attendeeDTO.email())).entity(attendeeDTO).build();
  }

}

```

- Create the AttendeeService in the attendes/domain/services package
    - create one method, "registerAttendee" that takes a RegisterAttendeeCommand

```java
package domain.services;

import dddhexagonalworkshop.conference.attendees.infrastrcture.AttendeeDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AttendeeService {

    public AttendeeDTO registerAttendee(RegisterAttendeeCommand registerAttendeeAttendeeCommand) {
        // Logic to register an attendee
        // This is a placeholder implementation
        return new AttendeeDTO(registerAttendeeAttendeeCommand.email());
    }
}

```

- Create the AttendeeEntity in the attendees/persistence package
    - only one field, "email"

```java
package dddhexagonalworkshop.conference.attendees.persistence;

import dddhexagonalworkshop.conference.attendees.api.AddressDTO;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.Badge;
import jakarta.persistence.*;

@Entity @Table(name = "attendee")
public class AttendeeEntity {

    @Id @GeneratedValue
    private Long id;

    private String email;

    protected AttendeeEntity() {

    }

    protected AttendeeEntity(String email) {
        this.email = email;
    }

    protected Long getId() {
        return id;
    }

    protected String getEmail() {
        return email;
    }

}
```

- Create the AttendeeRegistredEvent in the domain/events package
    - create a single field, "email"

```java
package dddhexagonalworkshop.conference.attendees.domain.events;

public record AttendeeRegisteredEvent(String email) {
}
```

- Create the AttendeeRegistrationResult in the attendees/domain/services package
    - create two fields, "attendee" and "attendeeRegistrationEvent"
```java
package dddhexagonalworkshop.conference.attendees.domain.services;

import dddhexagonalworkshop.conference.attendees.domain.Attendee;
import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;

public record AttendeeRegistrationResult(Attendee attendee, AttendeeRegisteredEvent attendeeRegisteredEvent) {
}
```

- Create the Attendee Aggregate in attendees/domain/aggregates
    - create a single method, "registerAttendee"
    - implement the method
        - by creating an AttendeeEntity and an AttendeeRegistredEvent
        - Create the AttendeeRegistrationResult in the attendees/domain/services package to return the AttendeeEntity and AttendeeRegisteredEvent
```java
package dddhexagonalworkshop.conference.attendees.domain;

public class Attendee {

  String email;

  public static Attendee registerAttendee(String email) {
    Attendee attendee = new Attendee();
    attendee.email = email;
    return attendee;
  }
  
  public String getEmail(){
    return email;
  }
}

```

- Create the AttendeeRepository using Hibernate Panache

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
    AttendeeEntity entity = new AttendeeEntity(attendee.getEmail());
    return entity;
  }
}
```

- Create the AttendeeEventPublisher
    - create a single method, "publish" that takes an AttendeeRegisteredEvent
    - implement the method by sending the event to Kafka

```java
package dddhexagonalworkshop.conference.attendees.infrastrcture;

import dddhexagonalworkshop.conference.attendees.domain.events.AttendeeRegisteredEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class AttendeeEventPublisher {

  @Channel("attendees")
  public Emitter<AttendeeRegisteredEvent> attendeesTopic;

  public void publish(AttendeeRegisteredEvent attendeeRegisteredEvent) {
    attendeesTopic.send(attendeeRegisteredEvent);
  }
}
```

- Update the AttendeeService so that it persists the attendee and publishes the event
    - update the registerAttendee method to return an AttendeeRegistratedResult
    - update the registerAttendee method to call the AttendeeEventPublisher

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
    AttendeeRegistrationResult result = Attendee.registerAttendee(registerAttendeeAttendeeCommand.email());


    //persist the attendee
    QuarkusTransaction.requiringNew().run(() -> {
      attendeeRepository.persist(result.attendee());
    });

    //notify the system that a new attendee has been registered
    attendeeEventPublisher.publish(result.attendeeRegisteredEvent());

    return new AttendeeDTO(result.attendee().getEmail());
  }
}
```

Update the AttendeeEndpoint to return the AttendeeDTO

```java
package dddhexagonalworkshop.conference.attendees.infrastrcture;

import dddhexagonalworkshop.conference.attendees.domain.services.AttendeeService;
import dddhexagonalworkshop.conference.attendees.domain.services.RegisterAttendeeCommand;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/attendees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendeeEndpoint {

    @Inject
    AttendeeService attendeeService;

    @POST
    public Response registerAttendee(RegisterAttendeeCommand registerAttendeeCommand) {
        Log.debugf("Creating attendee %s", registerAttendeeCommand);

        AttendeeDTO attendeeDTO = attendeeService.registerAttendee(registerAttendeeCommand);

        Log.debugf("Created attendee %s", attendeeDTO);

        return Response.created(URI.create("/" + attendeeDTO.email())).entity(attendeeDTO).build();
    }

}
```

## Summary
In this first iteration, we have created the basic structure of the Attendee registration micorservice.

### Key points
***Hexagonal Architecture/Ports and Adapters***: The AttendeeEndpoint is a _Port_ for the registering attendees.  In our case the _Adaper_ is the Jackson library, which is built into Quarkus, and handles converting JSON to Java objects and vice versa.  
The AttendeeEventPubliser is also an Adapter that sends events to Kafka, which is another Port in our architecture.  
The AttendeeRepository is a Port that allows us to persist the AttendeeEntity to a database.

***Aggregates*** Business logic is implemented in an Aggregate, Attendee. The Aggregate is responsible for creating the AttendeeEntity and the AttendeeRegisteredEvent.

***Commands*** we use a Command object, RegisterAttendeeCommand, to encapsulate the data needed to register an attendee.  Commands are different from Events because Commands can fail or be rejected, while Events are statements of fact that have already happened.

***Events*** we use an Event, AttendeeRegisteredEvent, to notify other parts of the system that an attendee has been registered.  Events are statements of fact that have already happened and cannot be changed.
