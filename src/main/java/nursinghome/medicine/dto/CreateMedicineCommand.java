package nursinghome.medicine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.medicine.model.Type;

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
    @Schema(description = "Type of medicine (TABLET, INJECTION, DROPS, CREAM)", required = true, example = "TABLET")
    private Type type;

    @NotNull
    private long residentId;

    public CreateMedicineCommand(String name, int dailyDose, Type type) {
        this.name = name;
        this.dailyDose = dailyDose;
        this.type = type;
    }
}
