package nursinghome.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.room.model.Capacity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomNameDto {

    private long id;

    private Capacity capacity;

    private String roomNumber;

}
