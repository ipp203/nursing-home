package nursinghome.service;

import nursinghome.model.EntityNotFoundException;
import nursinghome.model.medicine.Medicine;
import nursinghome.model.medicine.dto.CreateMedicineCommand;
import nursinghome.model.medicine.dto.MedicineDto;
import nursinghome.model.medicine.dto.UpdateDailyDoseCommand;
import nursinghome.model.resident.Resident;
import nursinghome.repository.MedicineRepository;
import nursinghome.repository.ResidentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
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
    public MedicineDto createMedicine(long residentId, CreateMedicineCommand command) {
        Resident resident = residentRepository.findLiveResidentById(residentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("residents/resident-not-found"),
                        "Resident not found",
                        "Resident with id not found, id: " + residentId));

        Medicine medicine = new Medicine(command.getName(), command.getDailyDose(), command.getType(), resident);
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
        return medicineRepository.findAll().stream()
                .filter(m -> name.isEmpty() || m.getName().toLowerCase().contains(name.get().toLowerCase()))
                .map(m -> modelMapper.map(m, MedicineDto.class))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> summarizeDailyDose() {
        return medicineRepository.findAll().stream()
                .collect(Collectors.toMap(m -> m.getName() + " " + m.getType(), Medicine::getDailyDose, Integer::sum));
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
