
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.LinkRecord;

@Repository
public interface LinkRecordRepository extends JpaRepository<LinkRecord, Integer> {

	@Query("select b from Brotherhood b join b.history h join h.linkRecords l where l.id = ?1")
	Brotherhood findBrotherhoodByLink(Integer id);

}
