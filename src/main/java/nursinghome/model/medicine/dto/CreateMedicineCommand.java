package nursinghome.model.medicine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.model.medicine.Type;
import nursinghome.model.resident.Resident;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMedicineCommand {
    @NotBlank
    @Schema(description = "Name of medicine", required = true, example = "Algopyrin")
    private String name;

    @Min(1)
    @Schema(description = "Daily dose of medicine", required = true, example = "3")
    private int dailyDose;

    @NotNull
    @Schema(description = "Type of medicine", required = true, example = "TABLET")
    private Type type;

}
