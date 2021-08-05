package nursinghome.resident.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import nursinghome.resident.model.ResidentStatus;

@Data
@AllArgsConstructor
public class StatusNumber {

    private ResidentStatus status;

    private Long number;
}
