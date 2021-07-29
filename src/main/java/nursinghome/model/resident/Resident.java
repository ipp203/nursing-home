package nursinghome.model.resident;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private ResidentStatus status;

    public Resident(String name, LocalDate dateOfBirth, Gender gender, ResidentStatus status) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = status;
    }
}
