# Workshop Workflow

## Iteration 03: Add Integration with External Service

### Overview
In this iteration we will add functionality for integration with an external service.

### Steps

#### 1. Create an Anit-Corruption Layer

Create a package, "salesteam.anticorruption"

In the anticorruption package create a Java record to represent the following JSON document:

```json
{
  "customers": [{
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "employer": "string",
      "customerDetails": {
        "dietaryRequirements": "VEGETARIAN|GLUTEN_FREE|NONE",
        "size": "XS|S|M|L|XL|XXL"
      }
    }]
}
```

```java
package dddhexagonalworkshop.conference.attendees.salesteam.anticorruption;

public record Customer(String firstName,
                       String lastName,
                       String email,
                       String employer,
                       CustomerDetails customerDetails) {
}
```

```java
package dddhexagonalworkshop.conference.attendees.salesteam.anticorruption;

public record CustomerDetails(
        DietaryRequirements dietaryRequirements,
        Size size) {
}
```

```java
package dddhexagonalworkshop.conference.attendees.salesteam.anticorruption;

public enum Size {
    XS,S,M,L,XL,XXL;
}
```

````java
package dddhexagonalworkshop.conference.attendees.salesteam.anticorruption;

public enum DietaryRequirements {
    VEGETARIAN, GLUTEN_FREE, NONE;
}
````

```java
package dddhexagonalworkshop.conference.attendees.salesteam.anticorruption;

import java.util.List;

public record SalesteamRegistrationRequest(List<Customer> customers) {
}
```

```java
package dddhexagonalworkshop.conference.attendees.salesteam.anticorruption;

import dddhexagonalworkshop.conference.attendees.domain.services.RegisterAttendeeCommand;
import dddhexagonalworkshop.conference.attendees.domain.services.AttendeeService;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/salesteam")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SalesteamEndpoint {

    @Inject
    AttendeeService attendeeService;

    @POST
    public Response registerAttendees(SalesteamRegistrationRequest salesteamRegistrationRequest) {
        Log.debugf("Registering attendees for %s", salesteamRegistrationRequest);

        List<RegisterAttendeeCommand> commands = SalesteamToDomainTranslator.translate(salesteamRegistrationRequest.customers());
        commands.forEach(attendeeService::registerAttendee);
        return Response.accepted().build();
    }
}
```

```java
package dddhexagonalworkshop.conference.attendees.salesteam;

import dddhexagonalworkshop.conference.attendees.domain.services.RegisterAttendeeCommand;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.MealPreference;
import dddhexagonalworkshop.conference.attendees.domain.valueobjects.TShirtSize;

import java.util.List;

public class SalesteamToDomainTranslator {

    public static List<RegisterAttendeeCommand> translate(List<Customer> customers) {
        return customers.stream()
                .map(customer -> new RegisterAttendeeCommand(
                                customer.email(),
                                customer.firstName(),
                                customer.lastName(),
                                null,
                                mapDietaryRequirements(customer.customerDetails().dietaryRequirements()),
                                mapTShirtSize(customer.customerDetails().size()))).toList();
    }

    private static MealPreference mapDietaryRequirements(DietaryRequirements dietaryRequirements) {
        if (dietaryRequirements == null) {
            return MealPreference.NONE;
        }
        return switch (dietaryRequirements) {
            case VEGETARIAN -> MealPreference.VEGETARIAN;
            case GLUTEN_FREE -> MealPreference.GLUTEN_FREE;
            case NONE -> MealPreference.NONE;
        };
    }

    private static TShirtSize mapTShirtSize(Size size) {
        if (size == null) {
            return null;
        }
        return switch (size) {
            case Size.XS -> TShirtSize.S;
            case Size.S -> TShirtSize.S;
            case Size.M -> TShirtSize.M;
            case Size.L -> TShirtSize.L;
            case Size.XL -> TShirtSize.XL;
            case Size.XXL -> TShirtSize.XXL;
            default -> null;
        };
    }
}
c
```

