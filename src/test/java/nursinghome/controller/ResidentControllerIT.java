package nursinghome.controller;

import nursinghome.model.resident.Gender;
import nursinghome.model.resident.ResidentStatus;
import nursinghome.model.resident.dto.CreateResidentCommand;
import nursinghome.model.resident.dto.ResidentDto;
import nursinghome.model.resident.dto.UpdateResidentStatusCommand;
import nursinghome.model.room.dto.RoomDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/delete_tables.sql")
class ResidentControllerIT {

    private static final String BASE_URL = "/api/nursinghome/residents";

    @Autowired
    TestRestTemplate template;

    @Test
    void createResident() {
        ResidentDto result = template.postForObject(BASE_URL,
                new CreateResidentCommand("John Doe", LocalDate.of(1950, 10, 10), Gender.MALE),
                ResidentDto.class);

        assertEquals("John Doe", result.getName());
    }

    @Test
    void createTooYoungResident() {
        Problem result = template.postForObject(BASE_URL,
                new CreateResidentCommand("John Doe", LocalDate.of(1990, 10, 10), Gender.MALE),
                Problem.class);

        assertEquals(Status.BAD_REQUEST, result.getStatus());
    }

    @Test
    void getResidentById() {
        ResidentDto postedResident = postResident("John Doe", LocalDate.of(1950, 10, 10), Gender.MALE);

        ResidentDto resident = template.getForObject(BASE_URL + "/" + postedResident.getId(),
                ResidentDto.class);

        assertEquals("John Doe", resident.getName());
    }

    @Test
    void getResidentByIdWithWrongId() {
        ResidentDto postedResident = postResident("John Doe", LocalDate.of(1950, 10, 10), Gender.MALE);

        Problem problem = template.getForObject(BASE_URL + "/" + (postedResident.getId() + 1), Problem.class);

        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void listResidents() {
        postResident("John Doe", LocalDate.of(1950, 10, 10), Gender.MALE);
        postResident("Jane Doe", LocalDate.of(1950, 10, 10), Gender.FEMALE);
        postResident("Jack Doe", LocalDate.of(1950, 10, 10), Gender.MALE);

        List<ResidentDto> result = template.exchange(BASE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResidentDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(3)
                .extracting(ResidentDto::getName)
                .containsExactly("John Doe", "Jane Doe", "Jack Doe");

    }

    @Test
    void updateResidentStatusAndList() {
        ResidentDto resident1 = postResident("John Doe", LocalDate.of(1950, 10, 10), Gender.MALE);
        ResidentDto resident2 = postResident("Jane Doe", LocalDate.of(1950, 10, 10), Gender.FEMALE);

        template.put(BASE_URL + "/" + resident1.getId(),
                new UpdateResidentStatusCommand(ResidentStatus.MOVED_OUT));
        template.put(BASE_URL + "/" + resident2.getId(),
                new UpdateResidentStatusCommand(ResidentStatus.MOVED_OUT));

        List<ResidentDto> result = template.exchange(BASE_URL + "?status=MOVED_OUT&name=jane",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResidentDto>>() {}).getBody();

        assertThat(result)
                .hasSize(1)
                .extracting(ResidentDto::getName)
                .containsExactly("Jane Doe");

    }

    @Test
    void deleteResident() {
        ResidentDto resident = postResident("John Doe", LocalDate.of(1950, 10, 10), Gender.MALE);

        template.delete(BASE_URL + "/" + resident.getId());

        List<ResidentDto> result = template.exchange(BASE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResidentDto>>() { }).getBody();

        assertThat(result)
                .hasSize(0);
    }

    private ResidentDto postResident(String name, LocalDate dateOfBirth, Gender gender) {
        return template.postForObject(BASE_URL,
                new CreateResidentCommand(name, dateOfBirth, gender),
                ResidentDto.class);
    }
}