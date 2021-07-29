package nursinghome.service;

import nursinghome.model.resident.*;
import nursinghome.model.resident.dto.CreateResidentCommand;
import nursinghome.model.resident.dto.ResidentDto;
import nursinghome.model.resident.dto.UpdateResidentStatusCommand;
import nursinghome.repository.ResidentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResidentService {

    private final ModelMapper modelMapper;
    private final ResidentRepository repository;

    public ResidentService(ModelMapper modelMapper, ResidentRepository repository) {
        this.modelMapper = modelMapper;
        this.repository = repository;
    }

    @Transactional
    public ResidentDto createResident(CreateResidentCommand command) {
        Resident resident = new Resident(command.getName(), command.getDateOfBirth(), command.getGender(), ResidentStatus.RESIDENT);
        repository.save(resident);
        return modelMapper.map(resident, ResidentDto.class);
    }

    public ResidentDto getResidentById(long id) {
        Resident resident = repository.findById(id).orElseThrow(() -> new ResidentNotFoundException(id));
        return modelMapper.map(resident, ResidentDto.class);
    }

    public List<ResidentDto> listResidents(Optional<ResidentStatus> status,
                                           Optional<Integer> birthBeforeYear,
                                           Optional<String> name) {
        return repository.findAll().stream()
                .filter(r -> status.isEmpty() || r.getStatus() == status.get())
                .filter(r -> birthBeforeYear.isEmpty() || r.getDateOfBirth().isBefore(LocalDate.of(birthBeforeYear.get(), 1, 1)))
                .filter(r->name.isEmpty() || r.getName().toLowerCase().contains(name.get().toLowerCase()))
                .map(r -> modelMapper.map(r, ResidentDto.class))
                .collect(Collectors.toList());
    }


    @Transactional
    public ResidentDto updateResidentStatus(long id, UpdateResidentStatusCommand command) {
        Resident resident = repository.findById(id).orElseThrow(() -> new ResidentNotFoundException(id));
        resident.setStatus(command.getStatus());
        return modelMapper.map(resident, ResidentDto.class);
    }

    @Transactional
    public void deleteResidentById(long id) {
        repository.deleteById(id);
    }

}
