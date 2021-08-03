package nursinghome.medicine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.medicine.model.Type;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto {

    private long id;

    private String name;

    private int dailyDose;

    private Type type;
}
