package nursinghome.controller;

import io.swagger.v3.oas.annotations.Operation;
import nursinghome.model.medicine.dto.CreateMedicineCommand;
import nursinghome.model.medicine.dto.MedicineDto;
import nursinghome.model.medicine.dto.UpdateDailyDoseCommand;
import nursinghome.service.MedicineService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/nursinghome/medicine")
public class MedicineController {

    private final MedicineService service;

    public MedicineController(MedicineService service) {
        this.service = service;
    }

    @PostMapping("/{residentId}")
    @Operation(summary = "Save medicine to resident")
    @ResponseStatus(HttpStatus.CREATED)
    public MedicineDto createMedicine(@Valid @RequestBody CreateMedicineCommand command, @PathVariable("residentId") long residentId) {
        return service.createMedicine(residentId, command);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update medicine daily dose")
    public MedicineDto updateDailyDose(@PathVariable("id") long id, @Valid @RequestBody UpdateDailyDoseCommand command) {
        return service.updateDailyDose(id, command);
    }

    @GetMapping
    @Operation(summary = "List all medicines")
    public List<MedicineDto> listMedicines(@RequestParam Optional<String> name) {
        return service.listMedicines(name);
    }

    @GetMapping("/dailysum")
    @Operation(summary = "Medicine doses summary by name and type")
    public Map<String, Integer> summarizeDailyDose() {
        return service.summarizeDailyDose();
    }

    @DeleteMapping("/{id}")
    public void deleteMedicineById(@PathVariable("id") long id){
        service.deleteMedicineById(id);
    }
}
