package nursinghome.medicine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDailyDoseCommand {
    @Min(1)
    @Schema(description = "Daily dose of medicine", required = true, example = "2")
    private int dailyDose;
}
