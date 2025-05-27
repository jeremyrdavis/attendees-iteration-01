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
