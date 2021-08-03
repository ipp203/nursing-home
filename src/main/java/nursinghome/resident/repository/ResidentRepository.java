package nursinghome.resident.repository;

import nursinghome.resident.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResidentRepository extends JpaRepository<Resident,Long> {

    @Query(value = "select r from Resident r where r.status = nursinghome.resident.model.ResidentStatus.RESIDENT and r.id = :id")
    Optional<Resident> findLiveResidentById(long id);

}
