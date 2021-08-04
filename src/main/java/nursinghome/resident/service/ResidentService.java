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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
                .collect(Collectors.toMap(Resident::getStatus, r -> 1, Integer::sum));
    }

    public RoomDto getResidentsRoom(long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> createNotFoundException(id));

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
}


//    public List<ResidentDto> listResidents(Optional<ResidentStatus> status,
//                                           Optional<Integer> birthBeforeYear,
//                                           Optional<String> name) {
//
//        @Data
//        @AllArgsConstructor
//        class QueryData {
//            String name;
//            Object filter;
//            String jpql;
//        }
//
//        List<QueryData> parameters = new ArrayList<>();
//
//        status.ifPresent(st -> parameters
//                .add(new QueryData("status", st, "r.status = :status ")));
//        birthBeforeYear.ifPresent(bby -> parameters
//                .add(new QueryData("datelimit", LocalDate.of(bby, 1, 1), "CAST(datelimit as date) r.dateofbirth < :datelimit ")));
//        name.ifPresent(n -> parameters.
//                add(new QueryData("name", "%" + n + "%", "r.name like :name")));
//
//
//        StringBuilder jpql = new StringBuilder("select r from Resident r ");
//
//        for (int i = 0; i < parameters.size(); i++) {
//            if (i == 0) {
//                jpql.append("where ");
//            } else {
//                jpql.append("and ");
//            }
//            jpql.append(parameters.get(i).getJpql());
//        }
//
//        List<Resident> result = new ArrayList<>();
//
//        switch (parameters.size()) {
//            case 0 -> {
//                result = entityManager
//                        .createQuery(jpql.toString(), Resident.class)
//                        .getResultList();
//            }
//            case 1 -> {
//                result = entityManager
//                        .createQuery(jpql.toString(), Resident.class)
//                        .setParameter(parameters.get(0).getName(), parameters.get(0).getFilter())
//                        .getResultList();
//            }
//            case 2 -> {
//                result = entityManager
//                        .createQuery(jpql.toString(), Resident.class)
//                        .setParameter(parameters.get(0).getName(), parameters.get(0).getFilter())
//                        .setParameter(parameters.get(1).getName(), parameters.get(1).getFilter())
//                        .getResultList();
//            }
//            case 3 -> {
//                result = entityManager
//                        .createQuery(jpql.toString(), Resident.class)
//                        .setParameter(parameters.get(0).getName(), parameters.get(0).getFilter())
//                        .setParameter(parameters.get(1).getName(), parameters.get(1).getFilter())
//                        .setParameter(parameters.get(2).getName(), parameters.get(2).getFilter())
//                        .getResultList();
//            }
//        }
//
//        Type targetListType = new TypeToken<List<ResidentDto>>() {}.getType();
//
//        return modelMapper.map(result, targetListType);
//    }