package nursinghome.model.resident;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResidentNotFoundException extends AbstractThrowableProblem {

    public ResidentNotFoundException(long id) {

        super(URI.create("residents/resident-not-found"),
                "Resident not found",
                Status.NOT_FOUND,
                "Resident with id not found, id: " + id);

    }

}
