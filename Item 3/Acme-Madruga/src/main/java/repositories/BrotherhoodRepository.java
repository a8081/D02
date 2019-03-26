
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Brotherhood;

public interface BrotherhoodRepository extends JpaRepository<Brotherhood, Integer> {

	@Query("select b from Brotherhood b where b.userAccount.id=?1")
	Brotherhood findByUserId(Integer brotherhoodId);

	@Query("select b from Enrolment e join e.brotherhood b where e.member.userAccount.id=?1 and e.dropOut=null")
	Collection<Brotherhood> findAllBrotherHoodByMember(Integer id);

	@Query("select count(e) from Enrolment e where e.dropOut=null group by e.member")
	Collection<Integer> getNumberOfMembersPerBrotherhood();

	@Query(value = "SELECT STDDEV(x),MAX(x),MIN(x),AVG(x) FROM (SELECT COUNT(*) AS x FROM `acme-parade`.ENROLMENT WHERE ENROLMENT.drop_out IS NULL GROUP BY brotherhood) AS x", nativeQuery = true)
	Double[] getStatisticsOfMembersPerBrotherhood();

	/** The largest brotherhood is the one with highest number of members **/
	@Query(
		value = "SELECT ENROLMENT.brotherhood FROM `acme-parade`.ENROLMENT INNER JOIN BROTHERHOOD  WHERE ENROLMENT.drop_out IS NULL GROUP BY brotherhood HAVING COUNT(*) = (SELECT MAX(x) FROM (SELECT COUNT(*) AS x FROM `acme-madruga`.ENROLMENT GROUP BY brotherhood)AS T)",
		nativeQuery = true)
	Integer[] getLargestBrotherhood();

	/** The smallest brotherhood is the one with lowest number of members **/
	@Query(
		value = "SELECT ENROLMENT.brotherhood FROM `acme-parade`.ENROLMENT INNER JOIN BROTHERHOOD WHERE ENROLMENT.drop_out IS NULL GROUP BY brotherhood HAVING COUNT(*) = (SELECT MIN(x) FROM (SELECT COUNT(*) AS x FROM `acme-madruga`.ENROLMENT GROUP BY brotherhood)AS T)",
		nativeQuery = true)
	Integer[] getSmallestBrotherhood();

	@Query("select distinct b from Enrolment e join e.brotherhood b where e.member.userAccount.id=?1 and e.dropOut!=null")
	Collection<Brotherhood> brotherhoodsHasBelonged(Integer memberUAId);

	@Query("select b from Brotherhood b join b.history h where h.id=?1")
	Brotherhood findBrotherhoodByHistory(int historyId);

	@Query("select p.brotherhood from Parade p where p.id=?1 ")
	Brotherhood findBrotherhoodByParade(int idParade);

	@Query("select b from Parade p join p.brotherhood b join p.floats f where f.id=?1")
	Brotherhood findBrotherhoodByFloat(int floatId);

}
