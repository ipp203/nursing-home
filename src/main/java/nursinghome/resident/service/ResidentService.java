package nursinghome.resident.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import nursinghome.EntityNotFoundException;
import nursinghome.resident.dto.CreateResidentCommand;
import nursinghome.resident.dto.ResidentDto;
import nursinghome.resident.dto.ResidentWithMedicinesDto;
import nursinghome.resident.dto.UpdateResidentStatusCommand;
import nursinghome.resident.model.Resident;
import nursinghome.resident.model.ResidentStatus;
import nursinghome.resident.repository.ResidentRepository;
import nursinghome.resident.repository.StatusNumber;
import nursinghome.room.dto.RoomDto;
import nursinghome.medicine.repository.MedicineRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResidentService {

    @Data
    @AllArgsConstructor
    class QueryData {
        String name;
        Object filter;
        String jpql;
    }


    private final ModelMapper modelMapper;
    private final ResidentRepository residentRepository;
    private final MedicineRepository medicineRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public ResidentService(ModelMapper modelMapper, ResidentRepository residentRepository, MedicineRepository medicineRepository, EntityManager entityManager) {
        this.modelMapper = modelMapper;
        this.residentRepository = residentRepository;
        this.medicineRepository = medicineRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public ResidentDto createResident(CreateResidentCommand command) {
        Resident resident = new Resident(command.getName(), command.getDateOfBirth(), command.getGender(), ResidentStatus.RESIDENT);
        residentRepository.save(resident);
        return modelMapper.map(resident, ResidentDto.class);
    }

    public ResidentWithMedicinesDto getResidentById(long id) {
        Resident resident = residentRepository.findById(id).orElseThrow(() -> createNotFoundException(id));
        return modelMapper.map(resident, ResidentWithMedicinesDto.class);
    }


    @Transactional
    public ResidentDto updateResidentStatus(long id, UpdateResidentStatusCommand command) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> createNotFoundException(id));

        resident.setStatus(command.getStatus());
        if (command.getStatus() != ResidentStatus.RESIDENT) {
            medicineRepository.deleteMedicinesByResident(resident);
            resident.setRoom(null);
        }
        return modelMapper.map(resident, ResidentDto.class);
    }

    @Transactional
    public void deleteResidentById(long id) {
        Resident resident = residentRepository.findById(id).orElseThrow(() -> createNotFoundException(id));
        residentRepository.delete(resident);
    }

    public Map<ResidentStatus, Long> getResidentSummaryByStatus() {
        return residentRepository.groupResidentsByStatus().stream()
                .collect(Collectors.toMap(StatusNumber::getStatus, StatusNumber::getNumber));
    }

    public RoomDto getResidentsRoom(long id) {
        Resident resident = residentRepository.findById(id).orElseThrow(() -> createNotFoundException(id));

        if (resident.getRoom() == null) {
            throw new EntityNotFoundException(URI.create("residents/resident-not-has-room"),
                    "Resident has no room.",
                    String.format("Resident with %d id doesn't have room", id));
        }
        return modelMapper.map(resident.getRoom(), RoomDto.class);
    }

    private EntityNotFoundException createNotFoundException(long residentId) {
        return new EntityNotFoundException(
                URI.create("residents/resident-not-found"),
                "Resident not found",
                "Resident with id not found, id: " + residentId);
    }


    public List<ResidentDto> listResidents(Optional<ResidentStatus> status,
                                           Optional<Integer> birthBeforeYear,
                                           Optional<String> name) {

        List<QueryData> parameters = getQueryData(status, birthBeforeYear, name);
        TypedQuery<Resident> query = getQueryByQueryData(parameters);
        return query.getResultList().stream()
                .map(r -> modelMapper.map(r, ResidentDto.class))
                .collect(Collectors.toList());
    }

    private List<QueryData> getQueryData(Optional<ResidentStatus> status, Optional<Integer> birthBeforeYear, Optional<String> name) {
        List<QueryData> parameters = new ArrayList<>();

        status.ifPresent(st -> parameters
                .add(new QueryData("status", st, "and r.status = :status ")));
        birthBeforeYear.ifPresent(bby -> parameters
                .add(new QueryData("datelimit", LocalDate.of(bby, 1, 1), "and r.dateOfBirth < :datelimit ")));
        name.ifPresent(n -> parameters.
                add(new QueryData("name", "%" + n + "%", "and r.name like :name")));
        return parameters;
    }

    private TypedQuery<Resident> getQueryByQueryData(List<QueryData> parameters) {
        StringBuilder jpqlQuery = new StringBuilder("select r from Resident r where 1=1 ");
        for (QueryData parameter : parameters) {
            jpqlQuery.append(parameter.getJpql());
        }

        TypedQuery<Resident> query = entityManager.createQuery(jpqlQuery.toString(), Resident.class);
        for (QueryData parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getFilter());
        }
        return query;
    }
}