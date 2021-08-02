package nursinghome.service;

import nursinghome.model.EntityNotFoundException;
import nursinghome.model.resident.Resident;
import nursinghome.model.resident.dto.ResidentDto;
import nursinghome.model.room.Room;
import nursinghome.model.room.RoomIsFullException;
import nursinghome.model.room.RoomNotEmptyException;
import nursinghome.model.room.dto.CreateRoomCommand;
import nursinghome.model.room.dto.RoomDto;
import nursinghome.repository.ResidentRepository;
import nursinghome.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ResidentRepository residentRepository;
    private final ModelMapper modelMapper;

    public RoomService(RoomRepository roomRepository, ResidentRepository residentRepository, ModelMapper modelMapper) {
        this.roomRepository = roomRepository;
        this.residentRepository = residentRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public RoomDto createRoom(CreateRoomCommand command) {
        Room room = roomRepository.save(new Room(command.getCapacity(), command.getRoomNumber()));
        return modelMapper.map(room, RoomDto.class);
    }

    public List<ResidentDto> listResidents(long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> createNotFoundException(id));

        Type targetListType = new TypeToken<List<ResidentDto>>() {
        }.getType();
        return modelMapper.map(room.getResidents(), targetListType);
    }

    @Transactional
    public void deleteRoom(long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> createNotFoundException(id));

        if (room.getResidents().size() != 0) {
            throw new RoomNotEmptyException(id);
        }
        roomRepository.delete(room);
    }

    @Transactional
    public RoomDto addResident(long roomId, long residentId) {
        Resident resident = residentRepository.findLiveResidentById(residentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("residents/resident-not-found"),
                        "Resident not found",
                        "Resident with id not found, id: " + residentId));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> createNotFoundException(roomId));

        if (room.isFull()) {
            throw new RoomIsFullException(roomId);
        }
        room.addResident(resident);
        return modelMapper.map(room, RoomDto.class);
    }

    private EntityNotFoundException createNotFoundException(long roomId){
        return new EntityNotFoundException(
                URI.create("rooms/not-found"),
                "Room not found",
                "Room not found wth id: " + roomId);
    }
}
