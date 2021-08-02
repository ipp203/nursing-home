package nursinghome.model.room;

import lombok.*;
import nursinghome.model.resident.Resident;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Capacity capacity;

    @Column(nullable = false, unique = true)
    private String roomNumber;

    @OneToMany(mappedBy = "room")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Resident> residents;

    public Room(Capacity capacity, String roomNumber) {
        this.capacity = capacity;
        this.roomNumber = roomNumber;
    }

    public boolean isFull() {
        return residents.size() == capacity.getNumberOfBeds();
    }

    public void addResident(Resident resident) {
        if (residents == null) {
            residents = new HashSet<>();
        }
        resident.setRoom(this);
        residents.add(resident);
    }

}
