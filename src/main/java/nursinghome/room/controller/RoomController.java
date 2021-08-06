package nursinghome.room.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nursinghome.resident.dto.ResidentDto;
import nursinghome.room.dto.CreateRoomCommand;
import nursinghome.room.dto.RoomDto;
import nursinghome.room.dto.RoomWithEmptyBedDto;
import nursinghome.room.dto.UpdateRoomResidentCommand;
import nursinghome.room.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/nursinghome/rooms")
@Tag(name = "Room Management")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create room")
    public RoomDto createRoom(@Valid @RequestBody CreateRoomCommand command){
        return service.createRoom(command);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get room's residents")
    public List<ResidentDto> listResidents(@PathVariable("id") long id){
        return service.listResidents(id);
    }

    @GetMapping
    @Operation(summary = "List rooms with number of empty bed")
    public List<RoomWithEmptyBedDto> listRooms(){
        return service.listRooms();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room if empty")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable("id") long id){
        service.deleteRoom(id);
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "Change resident's room")
    public RoomDto addResident(@PathVariable("roomId") long roomId, @RequestBody UpdateRoomResidentCommand command){
        return service.addResident(roomId, command);
    }

}
