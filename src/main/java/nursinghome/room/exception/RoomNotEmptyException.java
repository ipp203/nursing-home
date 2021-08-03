package nursinghome.room.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class RoomNotEmptyException extends AbstractThrowableProblem {

    public RoomNotEmptyException(long id) {
        super(URI.create("rooms/room-not-empty"),
                "Room is not empty",
                Status.PRECONDITION_FAILED,
                String.format("Room (id: %d) is not empty, can not delete",id));
    }

}
