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
