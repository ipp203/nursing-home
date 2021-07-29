package nursinghome.model.resident.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.model.resident.ResidentStatus;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResidentStatusCommand {

    @NotNull
    @Schema(required = true, description = "Status of resident (RESIDENT, MOVED, DIED)", example = "MOVED")
    private ResidentStatus status;
}
