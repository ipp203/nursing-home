package nursinghome;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import javax.annotation.Nullable;
import java.net.URI;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends AbstractThrowableProblem {
    public EntityNotFoundException(@Nullable URI type, @Nullable String title, @Nullable String detail) {
        super(type, title, Status.NOT_FOUND, detail);
    }
}
