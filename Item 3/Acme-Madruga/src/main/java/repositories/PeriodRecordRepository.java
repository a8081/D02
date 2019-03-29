
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.PeriodRecord;

@Repository
public interface PeriodRecordRepository extends JpaRepository<PeriodRecord, Integer> {

	@Query("select b from Brotherhood b join b.history h join h.periodRecords p where p.id = ?1")
	Brotherhood findBrotherhoodByPeriod(Integer id);

}
