
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

	@Query("select stddev(1.0 + (select count(e) from Enrolment e where e.brotherhood.id=b.id and e.dropOut=NULL) - 1.0), max(1.0 + (select count(e) from Enrolment e where e.brotherhood.id=b.id and e.dropOut=NULL) - 1.0), min(1.0 + (select count(e) from Enrolment e where e.brotherhood.id=b.id and e.dropOut=NULL) - 1.0), avg(1.0 + (select count(e) from Enrolment e where e.brotherhood.id=b.id and e.dropOut=NULL) - 1.0) from Brotherhood b")
	Double[] getStatisticsOfMembersPerBrotherhood();

	/** The largest brotherhood is the one with highest number of members **/
	@Query("select g from Brotherhood g where (1.0 + (select count(e) from Enrolment e where e.brotherhood.id=g.id and e.dropOut=NULL) - 1.0)=(select max(1.0 + (select count(en) from Enrolment en where en.brotherhood.id=b.id and en.dropOut=NULL) - 1.0) from Brotherhood b)")
	Brotherhood[] getLargestBrotherhood();

	/** The smallest brotherhood is the one with lowest number of members **/
	@Query("select g from Brotherhood g where (1.0 + (select count(e) from Enrolment e where e.brotherhood.id=g.id and e.dropOut=NULL) - 1.0)=(select min(1.0 + (select count(en) from Enrolment en where en.brotherhood.id=b.id and en.dropOut=NULL) - 1.0) from Brotherhood b)")
	Brotherhood[] getSmallestBrotherhood();

	@Query("select distinct b from Enrolment e join e.brotherhood b where e.member.userAccount.id=?1 and e.dropOut!=null")
	Collection<Brotherhood> brotherhoodsHasBelonged(Integer memberUAId);

	@Query("select b from Brotherhood b join b.history h where h.id=?1")
	Brotherhood findBrotherhoodByHistory(int historyId);

	@Query("select p.brotherhood from Parade p where p.id=?1 ")
	Brotherhood findBrotherhoodByParade(int idParade);

	@Query("select req.parade.brotherhood from Request req where req.id=?1")
	Brotherhood findByRequestId(int requestId);

	@Query("select e.brotherhood from Enrolment e where e.id=?1")
	Brotherhood findByEnrolmentId(int enrolmentId);

	@Query("select f.brotherhood from Float f where f.id=?1")
	Brotherhood findBrotherhoodByFloat(int floatId);

	@Query("select l.linkedBrotherhood from LinkRecord l join l.linkedBrotherhood b where b.id=?1")
	Collection<Brotherhood> allBrotherhoodsLinkByHistory(int brotherhoodId);

}
