package nursinghome.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nursinghome.resident.dto.ResidentDto;
import nursinghome.room.model.Capacity;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private long id;

    private Capacity capacity;

    private String roomNumber;

    @EqualsAndHashCode.Exclude
    private Set<ResidentDto> residents;
}
