package nursinghome.resident.repository;

import nursinghome.resident.model.Resident;
import nursinghome.resident.model.ResidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResidentRepository extends JpaRepository<Resident,Long> {

    Optional<Resident> findResidentByStatusAndId(ResidentStatus status,Long id);

    @Query(value = "select new nursinghome.resident.repository.StatusNumber(r.status, count(r.id)) from Resident r group by r.status order by r.status")
    List<StatusNumber> groupResidentsByStatus();

}
