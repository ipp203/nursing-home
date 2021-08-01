package nursinghome.model.resident.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.model.medicine.dto.MedicineDto;
import nursinghome.model.resident.Gender;
import nursinghome.model.room.dto.RoomDto;


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

    private RoomDto room;
}
