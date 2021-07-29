package nursinghome.model.resident.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import nursinghome.model.resident.Gender;
import nursinghome.model.resident.validator.DateOfBirth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateResidentCommand {

    @NotBlank
    @Schema(description = "Name of new resident", required = true, example = "John Doe")
    private String name;


    @NotNull
    @Schema(required = true, example = "1950-10-10")
    @DateOfBirth
    private LocalDate dateOfBirth;

    @NotNull
    @Schema(required = true, example = "MALE")
    private Gender gender;

}
