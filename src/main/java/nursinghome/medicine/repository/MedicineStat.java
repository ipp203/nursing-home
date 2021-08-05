package nursinghome.medicine.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import nursinghome.medicine.model.Type;


public class MedicineStat {

    private String name;

    private Type type;

    private Long number;

    public MedicineStat(String name, Type type, Long number) {
        this.name = name;
        this.type = type;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Long getNumber() {
        return number;
    }
}
