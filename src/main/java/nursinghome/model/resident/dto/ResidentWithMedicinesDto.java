package nursinghome.model.resident.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.model.medicine.Medicine;
import nursinghome.model.medicine.dto.MedicineDto;
import nursinghome.model.resident.Gender;
import nursinghome.model.resident.ResidentStatus;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentWithMedicinesDto {
    private long id;

    private String name;

    private LocalDate dateOfBirth;

    private Gender gender;

    private Set<MedicineDto> medicines;
}
