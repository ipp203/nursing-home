package nursinghome.model.medicine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.model.resident.Resident;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

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
    private Resident resident;

    public Medicine(String name, int dailyDose, Type type, Resident resident) {
        this.name = name;
        this.dailyDose = dailyDose;
        this.type = type;
        this.resident = resident;
    }


}
