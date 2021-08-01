package nursinghome.controller;

import io.swagger.v3.oas.annotations.Operation;
import nursinghome.model.resident.dto.ResidentDto;
import nursinghome.model.room.dto.CreateRoomCommand;
import nursinghome.model.room.dto.RoomDto;
import nursinghome.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nursinghome/room")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create room")
    public RoomDto createRoom(@RequestBody CreateRoomCommand command){
        return service.createRoom(command);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get residents of room")
    public List<ResidentDto> listResidents(@PathVariable("id") long id){
        return service.listResidents(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room if empty")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable("id") long id){
        service.deleteRoom(id);
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "Change resident's room")
    public RoomDto addResident(@PathVariable("roomId") long roomId, @RequestParam(required = true) long residentId){
        return service.addResident(roomId, residentId);
    }

}
