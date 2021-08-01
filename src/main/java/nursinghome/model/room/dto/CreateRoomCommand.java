package nursinghome.model.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nursinghome.model.room.Capacity;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomCommand {

    @NotNull
    @Schema(description = "Room number", required = true, example = "101")
    private String roomNumber;


    @NotNull
    @Schema(description = "Capacity of room ( SINGLE, DOUBLE, TRIPLE, FOUR_BED)",
            required = true,
            example = "DOUBLE")
    private Capacity capacity;
}
