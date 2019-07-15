
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {

	@Query("select p from Parade p where p.brotherhood.userAccount.id=?1")
	Collection<Parade> findAllParadeByBrotherhoodId(Integer broUAId);

	@Query("select p from Request r join r.parade p where r.member.userAccount.id=?1")
	Collection<Parade> findAllParadeByBMemberId(Integer memberUAId);

	@Query("select p from Parade p where p.ticker = ?1")
	Collection<Parade> getParadeWithTicker(String ticker);

	@Query("select p from Parade p where p.mode = 'FINAL'")
	Collection<Parade> findAllFinalMode();

	@Query("select p from Parade p where (p.mode = 'FINAL' AND ((1.0*(select count(e) from Enrolment e where e.member.id=?1 and e.brotherhood.id=p.brotherhood.id and e.enrolled=TRUE))>0))")
	Collection<Parade> findAllAvailableByMemberId(Integer id);

	@Query("select distinct p from Parade p where p.mode='FINAL' AND (?1='' OR p.description LIKE CONCAT('%',?1,'%') OR p.title LIKE CONCAT('%',?1,'%') OR p.ticker LIKE CONCAT('%',?1,'%')) AND (?4='' OR (?4=p.brotherhood.area.name)) AND ((p.moment>=?2) OR ?2=NULL) AND ((p.moment<=?3) OR ?3=NULL)")
	Collection<Parade> findParades(String keyword, Date minDate, Date maxDate, String area);

	@Query("select p from Parade p where p.mode='FINAL' AND p.brotherhood.id=?1")
	Collection<Parade> findAllFinalModeByBrotherhood(int userAccountId);

	@Query("select p from Parade p where p.status='ACCEPTED' AND p.brotherhood.id=?1")
	Collection<Parade> findAllAcceptedByBrotherhood(int brotherhoodId);

	@Query("select p from Parade p where p.status='REJECTED' AND p.brotherhood.id=?1")
	Collection<Parade> findAllRejectedByBrotherhood(int brotherhoodId);

	@Query("select p from Parade p where p.status='SUBMITTED' AND p.brotherhood.id=?1")
	Collection<Parade> findAllSubmittedByBrotherhood(int brotherhoodId);

	@Query("select p from Parade p where p.status='DEFAULT' AND p.brotherhood.id=?1")
	Collection<Parade> findAllDefaultByBrotherhood(int brotherhoodId);

	@Query("select p from Parade p where p.mode='FINAL' AND p.status='ACCEPTED' AND p.brotherhood.area.id=?1")
	Collection<Parade> findAllFinalModeAcceptedByArea(int areaId);

	@Query("select p from Parade p where p.mode='FINAL' AND p.status='REJECTED' AND p.brotherhood.area.id=?1")
	Collection<Parade> findAllFinalModeRejectedByArea(int areaId);

	@Query("select p from Parade p where p.mode='FINAL' AND p.status='SUBMITTED' AND p.brotherhood.area.id=?1")
	Collection<Parade> findAllFinalModeSubmittedByArea(int areaId);

	@Query("select sum(case when p.mode='DRAFT' then 1.0 else 0.0 end) / sum(case when p.mode='FINAL' then 1.0 else 0.0 end) from Parade p")
	Double findRatioDraftVsFinalParades();

	@Query("select sum(case when p.status='SUBMITTED' and p.mode='FINAL' then 1.0 else 0.0 end) / count(p) from Parade p")
	Double findSubmittedParadesRatio();

	@Query("select sum(case when p.status='ACCEPTED' and p.mode='FINAL' then 1.0 else 0.0 end) / count(p) from Parade p")
	Double findAcceptedParadesRatio();

	@Query("select sum(case when p.status='REJECTED' and p.mode='FINAL' then 1.0 else 0.0 end) / count(p) from Parade p")
	Double findRejectedParadesRatio();

	@Query("select p from Parade p join p.segments s where s.id=?1")
	Parade findParadeBySegment(int segmentId);

	@Query("select p from Parade p where p.status='ACCEPTED'")
	Collection<Parade> findAllAccepted();

	@Query("select sp.parade from Sponsorship sp where sp.sponsor.userAccount.id=?1")
	Collection<Parade> findAllParadeBySponsor(int sponsorUAId);

	@Query("select p from Parade p where p.mode='FINAL' AND p.brotherhood.area.id=?1")
	Collection<Parade> findAllFinalModeByArea(int areaId);

}
