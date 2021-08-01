package nursinghome.controller;

import nursinghome.model.medicine.Type;
import nursinghome.model.medicine.dto.CreateMedicineCommand;
import nursinghome.model.medicine.dto.MedicineDto;
import nursinghome.model.medicine.dto.UpdateDailyDoseCommand;
import nursinghome.model.resident.Gender;
import nursinghome.model.resident.dto.CreateResidentCommand;
import nursinghome.model.resident.dto.ResidentWithMedicinesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/delete_tables.sql")
class MedicineControllerIT {

    @Autowired
    TestRestTemplate template;

    CreateMedicineCommand med1;
    CreateMedicineCommand med2;
    CreateMedicineCommand med3;
    CreateMedicineCommand med4;

    ResidentWithMedicinesDto savedResident1;
    ResidentWithMedicinesDto savedResident2;

    @BeforeEach
    void init() {
        med1 = new CreateMedicineCommand("Algopyrin", 4, Type.TABLET);
        med2 = new CreateMedicineCommand("Nospa", 3, Type.TABLET);
        med3 = new CreateMedicineCommand("Nospa", 1, Type.INJECTION);
        med4 = new CreateMedicineCommand("Algopyrin", 3, Type.TABLET);

        savedResident1 = template.postForObject("/api/nursinghome/resident",
                new CreateResidentCommand("John Doe", LocalDate.of(1950, 10, 1), Gender.MALE),
                ResidentWithMedicinesDto.class);
        savedResident2 = template.postForObject("/api/nursinghome/resident",
                new CreateResidentCommand("Jane Doe", LocalDate.of(1953, 10, 1), Gender.FEMALE),
                ResidentWithMedicinesDto.class);
    }

    @Test
    void createMedicine() {
        template.postForObject("/api/nursinghome/medicine/" + savedResident1.getId(),
                new CreateMedicineCommand("Nurofen", 4, Type.DROPS),
                MedicineDto.class);

        ResidentWithMedicinesDto resident = template.getForObject("/api/nursinghome/resident/" + savedResident1.getId(),
                ResidentWithMedicinesDto.class);

        assertEquals(1, resident.getMedicines().size());
        assertEquals("Nurofen", ((MedicineDto) resident.getMedicines().toArray()[0]).getName());
    }

    @Test
    void createMedicineWithWrongResidentId() {
        long wrongId = savedResident2.getId() * 1000;
        Problem problem = template.postForObject("/api/nursinghome/medicine/" + wrongId,
                new CreateMedicineCommand("Nurofen", 4, Type.DROPS),
                Problem.class);

        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void updateDailyDose() {
        MedicineDto medicine = saveMedicine(savedResident1.getId(), med1);

        MedicineDto updatedMedicine = template.exchange("/api/nursinghome/medicine/" + medicine.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateDailyDoseCommand(2)),
                MedicineDto.class).getBody();

        assertNotNull(updatedMedicine);
        assertEquals(2, updatedMedicine.getDailyDose());
    }

    @Test
    void updateDailyDoseWithWrongId() {
        MedicineDto medicine = saveMedicine(savedResident1.getId(), med1);
        long wrongId = 1000 * medicine.getId();

        Problem problem = template.exchange("/api/nursinghome/medicine/" + wrongId,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateDailyDoseCommand(2)),
                Problem.class).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void listMedicines() {
        saveMedicine(savedResident1.getId(), med1);
        saveMedicine(savedResident1.getId(), med2);

        List<MedicineDto> medicines = template.exchange("/api/nursinghome/medicine",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MedicineDto>>() {
                }).getBody();

        assertThat(medicines)
                .hasSize(2)
                .extracting("name")
                .containsExactly("Algopyrin", "Nospa");
    }

    @Test
    void summarizeDailyDose() {
        saveMedicine(savedResident1.getId(), med1);
        saveMedicine(savedResident1.getId(), med2);
        saveMedicine(savedResident2.getId(), med3);
        saveMedicine(savedResident2.getId(), med4);

        Map<String, Integer> result = template.exchange("/api/nursinghome/medicine/dailysum",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Integer>>() {
                }).getBody();

        assertNotNull(result);
        assertThat(result.keySet())
                .hasSize(3)
                .contains("Algopyrin TABLET", "Nospa TABLET", "Nospa INJECTION");
    }

    @Test
    void deleteMedicineById() {
        MedicineDto medicine = saveMedicine(savedResident1.getId(), med1);

        ResidentWithMedicinesDto resident = template.getForObject("/api/nursinghome/resident/" + savedResident1.getId(),
                ResidentWithMedicinesDto.class);

        assertEquals(1, resident.getMedicines().size());

        template.delete("/api/nursinghome/medicine/" + medicine.getId());

        ResidentWithMedicinesDto residentWithDeletedMedicine = template.getForObject("/api/nursinghome/resident/" + savedResident1.getId(),
                ResidentWithMedicinesDto.class);

        assertEquals(0, residentWithDeletedMedicine.getMedicines().size());
    }

    @Test
    void deleteResidentDeleteOwnMedicine(){
        saveMedicine(savedResident1.getId(),med1);
        saveMedicine(savedResident1.getId(),med2);
        saveMedicine(savedResident2.getId(),med1);
        saveMedicine(savedResident2.getId(),med2);

        List<MedicineDto> initialMedicines = template.exchange("/api/nursinghome/medicine",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MedicineDto>>() {}).getBody();

        template.delete("/api/nursinghome/resident/" + savedResident1.getId());

        List<MedicineDto> medicines = template.exchange("/api/nursinghome/medicine",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MedicineDto>>() {}).getBody();

        assertNotNull(initialMedicines);
        assertEquals(4, initialMedicines.size());

        assertNotNull(medicines);
        assertEquals(2, medicines.size());
    }

    private MedicineDto saveMedicine(long residentId, CreateMedicineCommand medicine) {
        return template.postForObject("/api/nursinghome/medicine/" + residentId,
                medicine,
                MedicineDto.class);
    }
}