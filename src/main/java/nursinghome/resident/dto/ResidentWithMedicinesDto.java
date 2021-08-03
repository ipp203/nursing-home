package nursinghome.resident.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.medicine.dto.MedicineDto;
import nursinghome.resident.model.Gender;
import nursinghome.room.dto.RoomNameDto;


import java.time.LocalDate;
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

    private RoomNameDto room;
}
