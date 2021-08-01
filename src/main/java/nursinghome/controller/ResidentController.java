package nursinghome.controller;

import io.swagger.v3.oas.annotations.Operation;
import nursinghome.model.resident.dto.CreateResidentCommand;
import nursinghome.model.resident.dto.ResidentDto;
import nursinghome.model.resident.ResidentStatus;
import nursinghome.model.resident.dto.ResidentWithMedicinesDto;
import nursinghome.model.resident.dto.UpdateResidentStatusCommand;
import nursinghome.model.room.dto.RoomDto;
import nursinghome.service.ResidentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/nursinghome/resident")
public class ResidentController {

    private final ResidentService service;

    public ResidentController(ResidentService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create resident")
    @ResponseStatus(HttpStatus.CREATED)
    public ResidentDto createResident(@Valid @RequestBody CreateResidentCommand command) {
        return service.createResident(command);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resident and own medicines by id")
    public ResidentWithMedicinesDto getResidentById(@PathVariable("id") long id) {
        return service.getResidentById(id);
    }

    @GetMapping
    @Operation(summary = "List residents and filter by status and birth of year")
    public List<ResidentDto> listResidents(@RequestParam Optional<ResidentStatus> status,
                                           @RequestParam Optional<Integer> birthBeforeYear,
                                           @RequestParam Optional<String> name) {
        return service.listResidents(status, birthBeforeYear, name);
    }

    @GetMapping("/residentsummary")
    @Operation(summary = "Number of residents by status")
    public Map<ResidentStatus, Integer> getResidentSummaryByStatus() {
        return service.getResidentSummaryByStatus();
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update status and delete medicines if not resident")
    public ResidentDto updateResidentStatus(@PathVariable("id") long id,
                                            @Valid @RequestBody UpdateResidentStatusCommand command) {
        return service.updateResidentStatus(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete resident by id")
    public void deleteResident(@PathVariable("id") long id) {
        service.deleteResidentById(id);
    }


    @GetMapping("/{id}/room")
    @Operation(summary = "Get resident's room")
    public RoomDto getResidentRoom(@PathVariable("id") long id){
       return service.getResidentsRoom(id);
    }
}
