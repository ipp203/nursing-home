package nursinghome.controller;

import nursinghome.resident.model.Gender;
import nursinghome.resident.model.Resident;
import nursinghome.resident.model.ResidentStatus;
import nursinghome.resident.dto.ResidentDto;
import nursinghome.room.model.Capacity;
import nursinghome.room.dto.CreateRoomCommand;
import nursinghome.room.dto.RoomDto;
import nursinghome.resident.repository.ResidentRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/delete_tables.sql")
class RoomControllerIT {

    private static final String BASE_URL = "/api/nursinghome/rooms";

    @Autowired
    TestRestTemplate template;

    @Autowired
    ResidentRepository residentRepository;

    Resident resident1;
    Resident resident2;
    Resident movedOutResident;

    @BeforeEach
    void init() {
        resident1 = residentRepository.save(new Resident("John Doe",
                LocalDate.of(1950, 10, 1), Gender.MALE, ResidentStatus.RESIDENT));
        resident2 = residentRepository.save(new Resident("Jane Doe",
                LocalDate.of(1953, 10, 1), Gender.FEMALE, ResidentStatus.RESIDENT));
        movedOutResident = residentRepository.save(new Resident("Jack Doe",
                LocalDate.of(1951, 10, 1), Gender.FEMALE, ResidentStatus.MOVED_OUT));
    }

    @Test
    void createRoom() {
        RoomDto result = template.postForObject(BASE_URL,
                new CreateRoomCommand("101", Capacity.SINGLE),
                RoomDto.class);

        assertEquals("101", result.getRoomNumber());
    }

    @Test
    void addResident() {
        RoomDto room = saveRoom(new CreateRoomCommand("101", Capacity.SINGLE));

        RoomDto result = template.exchange(BASE_URL + "/{id}?residentId={residentId}",
                        HttpMethod.PUT,
                        null,
                        RoomDto.class,
                        Map.of("id", room.getId(), "residentId", resident1.getId()))
                .getBody();

        assertNotNull(result);
        assertEquals(1, result.getResidents().size());
    }

    @Test
    void addResidentThenChangeRoom() {
        RoomDto room1 = saveRoom(new CreateRoomCommand("101", Capacity.DOUBLE));
        RoomDto room2 = saveRoom(new CreateRoomCommand("102", Capacity.SINGLE));

        String putUrl = BASE_URL + "/{id}?residentId={residentId}";

        template.exchange(putUrl, HttpMethod.PUT, null, RoomDto.class,
                        Map.of("id", room1.getId(), "residentId", resident1.getId()))
                .getBody();

        RoomDto result = template.exchange(putUrl, HttpMethod.PUT, null, RoomDto.class,
                        Map.of("id", room1.getId(), "residentId", resident2.getId()))
                .getBody();

        assertNotNull(result);
        assertEquals(2, result.getResidents().size());

        RoomDto roomDto2 = template.exchange(putUrl, HttpMethod.PUT, null, RoomDto.class,
                        Map.of("id", room2.getId(), "residentId", resident1.getId()))
                .getBody();

        assertNotNull(roomDto2);
        assertEquals(1, roomDto2.getResidents().size());

        List<ResidentDto> room1Residents = template.exchange(BASE_URL + "/{id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResidentDto>>() {
                },
                result.getId()).getBody();

        assertNotNull(room1Residents);
        assertEquals(1, room1Residents.size());

    }

    @Test
    void addNotResidentResident() {
        RoomDto room = saveRoom(new CreateRoomCommand("101", Capacity.SINGLE));

        Problem problem = template.exchange(BASE_URL + "/{id}?residentId={residentId}",
                        HttpMethod.PUT,
                        null,
                        Problem.class,
                        room.getId(), movedOutResident.getId())
                .getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void listResidents() {
        RoomDto room1 = saveRoom(new CreateRoomCommand("101", Capacity.DOUBLE));
        String url = BASE_URL + "/{id}?residentId={residentId}";

        template.exchange(url, HttpMethod.PUT, null, RoomDto.class,
                room1.getId(), resident1.getId()).getBody();

        template.exchange(url, HttpMethod.PUT, null, RoomDto.class,
                room1.getId(), resident2.getId()).getBody();


        List<ResidentDto> room1Residents = template.exchange(BASE_URL + "/{id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResidentDto>>() {
                },
                room1.getId()).getBody();

        assertNotNull(room1Residents);
        assertEquals(2, room1Residents.size());

    }

    @Test
    void deleteEmptyRoom() {
        RoomDto room1 = saveRoom(new CreateRoomCommand("101", Capacity.DOUBLE));

        template.delete(BASE_URL + "/" + room1.getId());

        Problem problem = template.exchange(BASE_URL + "/{id}?residentId={residentId}",
                        HttpMethod.PUT,
                        null,
                        Problem.class,
                        room1.getId(), resident1.getId())
                .getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void deleteNotEmptyRoom() {
        RoomDto room1 = saveRoom(new CreateRoomCommand("101", Capacity.DOUBLE));

        template.exchange(BASE_URL + "/{id}?residentId={residentId}",
                        HttpMethod.PUT,
                        null,
                        RoomDto.class,
                        room1.getId(), resident1.getId())
                .getBody();


        Problem problem = template.exchange(BASE_URL + "/" + room1.getId(),
                HttpMethod.DELETE,
                null,
                Problem.class).getBody();

        assertNotNull(problem);
        assertEquals(Status.PRECONDITION_FAILED, problem.getStatus());
    }

    @Test
    void getResidentRoom() {
        RoomDto room1 = saveRoom(new CreateRoomCommand("101", Capacity.DOUBLE));

        String url = BASE_URL + "/{id}?residentId={residentId}";

        template.exchange(url, HttpMethod.PUT, null, RoomDto.class,
                room1.getId(), resident1.getId()).getBody();

        template.exchange(url, HttpMethod.PUT, null, RoomDto.class,
                room1.getId(), resident2.getId()).getBody();

        RoomDto roomDto = template.getForObject("/api/nursinghome/residents/{id}/room",
                RoomDto.class, resident1.getId());

        assertNotNull(roomDto);
        assertEquals(room1.getId(), roomDto.getId());
    }


    private RoomDto saveRoom(CreateRoomCommand command) {
        return template.postForObject(BASE_URL, command, RoomDto.class);
    }
}