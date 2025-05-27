package dddhexagonalworkshop.conference.attendees.persistence;

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
