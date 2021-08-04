package nursinghome.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.room.model.Capacity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomWithEmptyBedDto {

    private String roomNumber;

    private int numberOfEmptyBed;

    private Capacity capacity;
}
