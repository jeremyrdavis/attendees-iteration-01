# DDD Hexagonal Workshop Workflow Overview

This document provides a summary of the four iterations in our DDD (Domain-Driven Design) Hexagonal Architecture workshop. Each iteration builds upon the previous one, introducing new concepts and expanding the application's capabilities.

## Iteration 1: Core Domain Model

**Introduction:**
In this iteration, we will cover the basics of Domain-Driven Design by implementing a basic workflow for registering a conference attendee. We will create the following DDD constructs:
- Aggregate
- Domain Service
- Domain Event
- Command
- Adapter
- Entity
- Repository

**Steps:**
1. Create a `RegisterAttendeeCommand` with only one, basic property (email).
2. Implement an Adapter in the form of a REST Endpoint, `AttendeeEndpoint` with a POST method.
3. Implement a Service, `AttendeeService` that will orchestration the registration process.
4. Create an `Attendee` entity that represents the attendee in the domain and implements the application's invariants or business rules.
4. Create a Domain Event, `AttendeeRegisteredEvent`, that will be published when an attendee is successfully registered.
5. Create a Repository interface, `AttendeeRepository`, that defines methods for saving and retrieving attendees.
6. Create an Entity, `AttendeeEntity`, to persist instances of the `Attendee` entity in a database.
7. Create an Adapter, `AttendeeEventPublisher`, that sends events to Kafka to propagate changes to the rest of the system.

**Summary:**
By the end of Iteration 1, you'll have a solid foundation in DDD concepts and a very basic working application.

## Iteration 2: Hexagonal Architecture

**Introduction:**
This iteration introduces the Hexagonal Architecture (Ports and Adapters) pattern. You'll learn how to separate the domain model from external concerns by defining clear boundaries and interfaces.

**Steps:**
1. Create ports (interfaces) for repository and event publishing
2. Implement adapters for persistence using a database
3. Add adapters for the REST API endpoints
4. Connect the domain model to the infrastructure through ports
5. Configure dependency injection to wire everything together

**Summary:**
After completing Iteration 2, you'll understand how Hexagonal Architecture enables the separation of concerns, making your application more maintainable and testable. The domain model remains pure while adapters handle the technical details of persistence and communication.

## Iteration 3: Integration with External Systems

**Introduction:**
In this iteration, you'll learn how to integrate with external systems while maintaining the integrity of your domain model. You'll implement an anti-corruption layer to translate between different contexts.

**Steps:**
1. Create a model for the external sales team system
2. Implement a translator between the sales team model and your domain model
3. Add an endpoint to receive data from the sales team
4. Create integration tests for the new functionality
5. Ensure the domain model remains isolated from external influences

**Summary:**
By the end of Iteration 3, you'll understand how to protect your domain model when integrating with external systems. The anti-corruption layer ensures that concepts from other contexts don't leak into your domain, preserving its integrity and clarity.

## Iteration 4: Advanced Domain Modeling

**Introduction:**
The final iteration focuses on advanced domain modeling techniques to handle more complex business requirements. You'll learn about domain events, event sourcing, and CQRS (Command Query Responsibility Segregation).

**Steps:**
1. Enhance the domain model with additional business rules
2. Implement event sourcing for tracking attendee history
3. Apply CQRS to separate read and write operations
4. Add specialized query models for reporting
5. Implement eventual consistency between read and write models

**Summary:**
After completing Iteration 4, you'll have a sophisticated understanding of advanced DDD techniques for handling complex domains. The application now supports a richer set of business requirements while maintaining a clean architecture and separation of concerns.

## Conclusion

Through these four iterations, you've progressed from a basic domain model to a fully-featured application with a clean architecture that can accommodate complex business rules and integrate with external systems. The DDD and Hexagonal Architecture patterns have provided a solid foundation for building maintainable, testable, and flexible software that closely aligns with business needs.
