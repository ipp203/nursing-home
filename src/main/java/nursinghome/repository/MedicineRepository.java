package nursinghome.repository;

import nursinghome.model.medicine.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    @Query(value = "delete from Medicine m where m.resident.id = :id")
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByResidentId(long id);
}
