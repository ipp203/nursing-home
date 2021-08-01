package nursinghome.model.room;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomNotFoundException extends AbstractThrowableProblem {

    public RoomNotFoundException(long id) {
        super(URI.create("rooms/not-found"),
                "Room not found",
                Status.NOT_FOUND,
                "Room not found wth id: " + id);
    }

}
