
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.MiscellaneousRecord;

@Repository
public interface MiscellaneousRecordRepository extends JpaRepository<MiscellaneousRecord, Integer> {

	@Query("select b from Brotherhood b join b.history h join h.miscellaneousRecords m where m.id = ?1")
	Brotherhood findBrotherhoodByMiscellaneous(Integer id);

}
