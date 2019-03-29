
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.LegalRecord;

@Repository
public interface LegalRecordRepository extends JpaRepository<LegalRecord, Integer> {

	@Query("select b from Brotherhood b join b.history h join h.legalRecords l where l.id = ?1")
	Brotherhood findBrotherhoodByLegal(Integer id);
}
