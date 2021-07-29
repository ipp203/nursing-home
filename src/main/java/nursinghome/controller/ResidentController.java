package nursinghome.controller;

import io.swagger.v3.oas.annotations.Operation;
import nursinghome.model.resident.dto.CreateResidentCommand;
import nursinghome.model.resident.dto.ResidentDto;
import nursinghome.model.resident.ResidentStatus;
import nursinghome.model.resident.dto.UpdateResidentStatusCommand;
import nursinghome.service.ResidentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nursinghome/resident")
public class ResidentController {

    private ResidentService service;

    public ResidentController(ResidentService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create resident")
    public ResidentDto createResident(@Valid @RequestBody CreateResidentCommand command){
        return service.createResident(command);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resident by id")
    public ResidentDto getResidentById(@PathVariable("id") long id){
        return service.getResidentById(id);
    }

    @GetMapping
    @Operation(summary = "List residents and filter by status and birth of year")
    public List<ResidentDto> listResidents(@RequestParam Optional<ResidentStatus> status, @RequestParam Optional<Integer> birthBeforeYear ){
        return service.listResidents(status, birthBeforeYear);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update resident status")
    public ResidentDto updateResidentStatus(@PathVariable("id")long id, @Valid @RequestBody UpdateResidentStatusCommand command){
        return service.updateResidentStatus(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete resident by id")
    public void deleteResident(@PathVariable("id") long id){
        service.deleteResidentById(id);
    }
}
