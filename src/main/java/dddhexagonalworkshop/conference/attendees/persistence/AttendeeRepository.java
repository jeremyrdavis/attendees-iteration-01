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
