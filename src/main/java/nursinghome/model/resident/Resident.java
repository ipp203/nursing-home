package nursinghome.model.resident;

import lombok.*;
import nursinghome.model.medicine.Medicine;
import nursinghome.model.room.Room;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
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

    @ManyToOne
    private Room room;

    public Resident(String name, LocalDate dateOfBirth, Gender gender, ResidentStatus status) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = status;
    }

    public void addMedicine(Medicine medicine){
        if(medicines == null){
            medicines = new HashSet<>();
        }
        medicine.setResident(this);
        medicines.add(medicine);
    }
}
