package nursinghome.model.medicine;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MedicineNotFoundException extends AbstractThrowableProblem {

    public MedicineNotFoundException(long id) {
        super(URI.create("medicines/medicine-not-found"),
                "Medicine not found",
                Status.NOT_FOUND,
                "Medicine not found, id: " + id);
    }

}
