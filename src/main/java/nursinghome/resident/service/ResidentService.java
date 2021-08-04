package nursinghome.resident.service;

import nursinghome.EntityNotFoundException;
import nursinghome.resident.dto.CreateResidentCommand;
import nursinghome.resident.dto.ResidentDto;
import nursinghome.resident.dto.ResidentWithMedicinesDto;
import nursinghome.resident.dto.UpdateResidentStatusCommand;
import nursinghome.resident.model.Resident;
import nursinghome.resident.model.ResidentStatus;
import nursinghome.resident.repository.ResidentRepository;
import nursinghome.room.dto.RoomDto;
import nursinghome.medicine.repository.MedicineRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResidentService {

    private final ModelMapper modelMapper;
    private final ResidentRepository residentRepository;
    private final MedicineRepository medicineRepository;

    public ResidentService(ModelMapper modelMapper, ResidentRepository residentRepository, MedicineRepository medicineRepository) {
        this.modelMapper = modelMapper;
        this.residentRepository = residentRepository;
        this.medicineRepository = medicineRepository;
    }

    @Transactional
    public ResidentDto createResident(CreateResidentCommand command) {
        Resident resident = new Resident(command.getName(), command.getDateOfBirth(), command.getGender(), ResidentStatus.RESIDENT);
        residentRepository.save(resident);
        return modelMapper.map(resident, ResidentDto.class);
    }

    public ResidentWithMedicinesDto getResidentById(long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> createNotFoundException(id));

        return modelMapper.map(resident, ResidentWithMedicinesDto.class);
    }

    public List<ResidentDto> listResidents(Optional<ResidentStatus> status,
                                           Optional<Integer> birthBeforeYear,
                                           Optional<String> name) {
        return residentRepository.findAll().stream()
                .filter(r -> status.isEmpty() || r.getStatus() == status.get())
                .filter(r -> birthBeforeYear.isEmpty() || r.getDateOfBirth().isBefore(LocalDate.of(birthBeforeYear.get(), 1, 1)))
                .filter(r -> name.isEmpty() || r.getName().toLowerCase().contains(name.get().toLowerCase()))
                .map(r -> modelMapper.map(r, ResidentDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ResidentDto updateResidentStatus(long id, UpdateResidentStatusCommand command) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> createNotFoundException(id));

        resident.setStatus(command.getStatus());
        if (command.getStatus() != ResidentStatus.RESIDENT) {
            medicineRepository.deleteAllByResidentId(id);
            resident.setRoom(null);
        }
        return modelMapper.map(resident, ResidentDto.class);
    }

    @Transactional
    public void deleteResidentById(long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> createNotFoundException(id));

        residentRepository.delete(resident);
    }

    public Map<ResidentStatus, Integer> getResidentSummaryByStatus() {
        return residentRepository.findAll().stream()
                .collect(Collectors.toMap(Resident::getStatus,r->1,Integer::sum));
    }

    public RoomDto getResidentsRoom(long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(()-> createNotFoundException(id));

        if(resident.getRoom() == null){
            throw new EntityNotFoundException(URI.create("residents/resident-not-has-room"),
                    "Resident has no room.",
                    String.format("Resident with %d id doesn't have room", id));
        }
        return modelMapper.map(resident.getRoom(),RoomDto.class);
    }

    private EntityNotFoundException createNotFoundException(long residentId){
        return new EntityNotFoundException(
                URI.create("residents/resident-not-found"),
                "Resident not found",
                "Resident with id not found, id: " + residentId);
    }
}
