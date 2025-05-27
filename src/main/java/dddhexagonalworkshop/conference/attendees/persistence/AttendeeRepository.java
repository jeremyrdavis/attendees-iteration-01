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
