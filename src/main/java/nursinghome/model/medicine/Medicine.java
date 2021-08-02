package nursinghome.model.medicine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nursinghome.model.resident.Resident;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int dailyDose;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @ToString.Exclude
    private Resident resident;

    public Medicine(String name, int dailyDose, Type type, Resident resident) {
        this.name = name;
        this.dailyDose = dailyDose;
        this.type = type;
        this.resident = resident;
    }
}
