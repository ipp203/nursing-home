package nursinghome.model.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nursinghome.model.resident.dto.ResidentDto;
import nursinghome.model.room.Capacity;

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
