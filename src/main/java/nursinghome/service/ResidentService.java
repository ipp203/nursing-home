package nursinghome.service;

import nursinghome.model.resident.*;
import nursinghome.model.resident.dto.CreateResidentCommand;
import nursinghome.model.resident.dto.ResidentDto;
import nursinghome.model.resident.dto.ResidentWithMedicinesDto;
import nursinghome.model.resident.dto.UpdateResidentStatusCommand;
import nursinghome.model.room.dto.RoomDto;
import nursinghome.repository.ResidentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResidentService {

    private final ModelMapper modelMapper;
    private final ResidentRepository residentRepository;

    public ResidentService(ModelMapper modelMapper, ResidentRepository residentRepository) {
        this.modelMapper = modelMapper;
        this.residentRepository = residentRepository;
    }

    @Transactional
    public ResidentDto createResident(CreateResidentCommand command) {
        Resident resident = new Resident(command.getName(), command.getDateOfBirth(), command.getGender(), ResidentStatus.RESIDENT);
        residentRepository.save(resident);
        return modelMapper.map(resident, ResidentDto.class);
    }

    public ResidentWithMedicinesDto getResidentById(long id) {
        Resident resident = residentRepository.findById(id).orElseThrow(() -> new ResidentNotFoundException(id));
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
        Resident resident = residentRepository.findById(id).orElseThrow(() -> new ResidentNotFoundException(id));
        resident.setStatus(command.getStatus());
        if (command.getStatus() != ResidentStatus.RESIDENT) {
            resident.deleteMedicines();
            resident.setRoom(null);
        }
        return modelMapper.map(resident, ResidentDto.class);
    }

    @Transactional
    public void deleteResidentById(long id) {
        residentRepository.deleteById(id);
    }

    public Map<ResidentStatus, Integer> getResidentSummaryByStatus() {
        return residentRepository.findAll().stream()
                .collect(Collectors.toMap(Resident::getStatus,r->1,Integer::sum));
    }

    public RoomDto getResidentsRoom(long id) {
        Resident resident = residentRepository.findById(id).orElseThrow(()->new ResidentNotFoundException(id));
        if(resident.getRoom() == null){
            throw new ResidentNotHasRoomException();
        }
        return modelMapper.map(resident.getRoom(),RoomDto.class);
    }
}
