package nursinghome.model.resident;

import lombok.*;
import nursinghome.model.meal.Meal;
import nursinghome.model.medicine.Medicine;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "residents")
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private ResidentStatus status;

    @OneToMany(mappedBy = "resident", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Medicine> medicines;

    public Resident(String name, LocalDate dateOfBirth, Gender gender, ResidentStatus status) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = status;
    }

    public void addMedicine(Medicine medicine) {
        if (medicines == null) {
            medicines = new HashSet<>();
        }
        medicine.setResident(this);
        medicines.add(medicine);
    }

    public void deleteMedicines() {
        medicines.clear();
    }
}
