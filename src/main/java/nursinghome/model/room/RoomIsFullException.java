package nursinghome.model.room;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class RoomIsFullException extends AbstractThrowableProblem {

    public RoomIsFullException(long id) {
        super(URI.create("rooms/room-full"),
                "Room is full",
                Status.PRECONDITION_FAILED,
                "The room is full, there is no any free bed! Room id: " + id);
    }

}
