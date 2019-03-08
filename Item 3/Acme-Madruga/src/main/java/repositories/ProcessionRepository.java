
package repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Procession;

@Repository
public interface ProcessionRepository extends JpaRepository<Procession, Integer> {

	@Query("select p from Procession p where p.brotherhood.userAccount.id=?1")
	Collection<Procession> findAllProcessionByBrotherhoodId(Integer id);

	@Query("select p from Request r join r.procession p where r.member.userAccount.id=?1")
	Collection<Procession> findAllProcessionByBMemberId(Integer id);

	@Query("select p from Procession p where p.ticker = ?1")
	Collection<Procession> getProcessionWithTicker(String ticker);

	@Query("select p from Procession p where p.mode = 'FINAL'")
	Collection<Procession> findAllFinalMode();

	@Query(value = "select  * from `acme-madruga`.Procession WHERE timestampdiff(MINUTE, '2020-03-27', moment) <=30*24*60", nativeQuery = true)
	List<Procession> getProcessionsThirtyDays();

	@Query("select distinct p from Procession p where p.mode='FINAL' AND (?1='' OR p.description LIKE CONCAT('%',?1,'%') OR p.title LIKE CONCAT('%',?1,'%') OR p.ticker LIKE CONCAT('%',?1,'%')) AND (?4='' OR (?4=p.brotherhood.area.name)) AND ((p.moment>=?2) OR ?2=NULL) AND ((p.moment<=?3) OR ?3=NULL)")
	Collection<Procession> findProcessions(String keyword, Date minDate, Date maxDate, String area);

	@Query("select p from Procession p where p.mode='FINAL' AND p.brotherhood.id=?1")
	Collection<Procession> findAllFinalModeByBrotherhood(int userAccountId);
}
