package nursinghome.resident.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import nursinghome.resident.validator.DateOfBirth;
import nursinghome.resident.model.Gender;

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
    @Schema(required = true, example = "MALE", description = "Gender of resident (MALE, FEMALE)")
    private Gender gender;

}
