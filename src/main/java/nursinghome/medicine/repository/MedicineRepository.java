package nursinghome.medicine.repository;

import nursinghome.medicine.model.Medicine;
import nursinghome.medicine.model.Type;
import nursinghome.resident.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    void deleteMedicinesByResident(Resident resident);

    List<Medicine> findAllByNameContains (String name);

    @Query(value = "select new nursinghome.medicine.repository.MedicineStat(m.name, m.type, sum(m.dailyDose)) from Medicine m group by m.name, m.type order by m.name")
    List<MedicineStat> groupDailyDoses();
}
