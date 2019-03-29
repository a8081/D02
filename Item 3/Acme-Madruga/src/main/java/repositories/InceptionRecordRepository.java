
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.InceptionRecord;

@Repository
public interface InceptionRecordRepository extends JpaRepository<InceptionRecord, Integer> {

	@Query("select b from Brotherhood b join b.history h where h.inceptionRecord.id = ?1")
	Brotherhood findBrotherhoodByInception(Integer id);

}
