package nursinghome.resident.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResidentNotHasRoomException extends AbstractThrowableProblem {
    public ResidentNotHasRoomException() {
        super(URI.create("residents/resident-not-has-room"),
                "Resident has no room.",
                Status.NOT_FOUND,
                "Resident doesn't have room");
    }
}
