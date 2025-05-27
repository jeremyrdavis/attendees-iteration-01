package dddhexagonalworkshop.conference.attendees.salesteam;

public record Customer(String firstName,
                       String lastName,
                       String email,
                       String employer,
                       CustomerDetails customerDetails) {
}
