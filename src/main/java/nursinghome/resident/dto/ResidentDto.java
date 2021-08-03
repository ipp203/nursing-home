package nursinghome.resident.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.resident.model.Gender;
import nursinghome.resident.model.ResidentStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentDto {
    private long id;

    private String name;

    private LocalDate dateOfBirth;

    private Gender gender;

    private ResidentStatus status;
}
