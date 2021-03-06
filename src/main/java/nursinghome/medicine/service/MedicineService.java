package nursinghome.medicine.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import nursinghome.EntityNotFoundException;
import nursinghome.medicine.model.Medicine;
import nursinghome.medicine.repository.MedicineRepository;
import nursinghome.medicine.dto.CreateMedicineCommand;
import nursinghome.medicine.dto.MedicineDto;
import nursinghome.medicine.dto.UpdateDailyDoseCommand;
import nursinghome.medicine.repository.MedicineStat;
import nursinghome.resident.model.Resident;
import nursinghome.resident.model.ResidentStatus;
import nursinghome.resident.repository.ResidentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final ResidentRepository residentRepository;
    private final ModelMapper modelMapper;

    public MedicineService(MedicineRepository medicineRepository, ResidentRepository residentRepository, ModelMapper modelMapper) {
        this.medicineRepository = medicineRepository;
        this.residentRepository = residentRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public MedicineDto createMedicine(CreateMedicineCommand command) {
        Resident resident = residentRepository.findResidentByStatusAndId(ResidentStatus.RESIDENT, command.getResidentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("residents/resident-not-found"),
                        "Resident not found",
                        "Resident resident with id not found, id: " + command.getResidentId()));

        Medicine medicine = new Medicine(command.getName(), command.getDailyDose(), command.getType(), resident);
        medicineRepository.save(medicine);
        resident.addMedicine(medicine);
        return modelMapper.map(medicine, MedicineDto.class);
    }

    @Transactional
    public MedicineDto updateDailyDose(long id, UpdateDailyDoseCommand command) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("medicines/medicine-not-found"),
                        "Medicine not found",
                        "Medicine not found, id: " + id));

        medicine.setDailyDose(command.getDailyDose());
        return modelMapper.map(medicine, MedicineDto.class);
    }

    public List<MedicineDto> listMedicines(Optional<String> name) {
        if(name.isEmpty()) {
            return medicineRepository.findAll().stream()
                    .map(m -> modelMapper.map(m, MedicineDto.class))
                    .collect(Collectors.toList());
        }else{
            return medicineRepository.findAllByNameContains(name.get()).stream()
                    .map(m -> modelMapper.map(m, MedicineDto.class))
                    .collect(Collectors.toList());
        }
    }

    public Map<String, Long> summarizeDailyDose() {

        @Data
        @AllArgsConstructor
        class Stat{
            private String name;
            private Long number;
        }

        return medicineRepository.groupDailyDoses().stream()
                .map(ms -> new Stat(ms.getName() + " " + ms.getType(), ms.getNumber()))
                .collect(Collectors.toMap(Stat::getName, Stat::getNumber));


    }

    @Transactional
    public void deleteMedicineById(long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                URI.create("medicines/medicine-not-found"),
                "Medicine not found",
                "Medicine not found, id: " + id));
        medicineRepository.delete(medicine);
    }
}
