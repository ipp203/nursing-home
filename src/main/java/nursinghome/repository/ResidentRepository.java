package nursinghome.repository;

import nursinghome.model.resident.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentRepository extends JpaRepository<Resident,Long> {
}
