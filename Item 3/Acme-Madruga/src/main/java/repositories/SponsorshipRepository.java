
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select sp.sponsor from Sponsorship sp where sp.sponsor.userAccount.id=?1")
	Collection<Sponsorship> findAllByUserId(final Integer userAccountId);

	@Query("select sp.sponsor from Sponsorship sp where sp.sponsor.userAccount.id=?1 and sp.activated=1")
	Collection<Sponsorship> findAllActivateByUserId(final Integer userAccountId);

	@Query("select sp.sponsor from Sponsorship sp where sp.sponsor.userAccount.id=?1 and sp.activated=0")
	Collection<Sponsorship> findAllDeactivateByUserId(final Integer userAccountId);

	// DASHBOARD

	/** The ratio of active sponsorships */
	@Query("select sum(case when s.activated=1 then 1.0 else 0.0 end) / count(s) from Sponsorship s")
	Double getRatioActivatedSponsorships();

	/** The average, the minimum, the maximum and the standard deviation of the number of active sponsorships per sponsor */
	@Query("select avg(1.0+(select count(sp) from Sponsorship sp where sp.sponsor.id=s.id)-1.0), min(1.0+(select count(sp) from Sponsorship sp where sp.sponsor.id=s.id)-1.0), max(1.0+(select count(sp) from Sponsorship sp where sp.sponsor.id=s.id)-1.0), stddev(1.0+(select count(sp) from Sponsorship sp where sp.sponsor.id=s.id)-1.0) from Sponsor s)")
	Double[] getStatisticsOfActiveSponsorshipsPerSponsor();

	/** The ordered list of sponsors in terms of number of active sponsorships. Implemented as a List to get the top-5 as a sublist(0,5). */
	@Query("select s from Sponsor s order by 1.0+(select count(sp) from Sponsorship sp where sp.sponsor.id=s.id and sp.activated=1) desc")
	List<Sponsorship> getActiveSponsorships();

	@Query("select s from Sponsorship s where s.parade.id=?1")
	Collection<Sponsorship> findByParade(int paradeId);

	@Query("select s from Sponsorship s where s.parade.id=?1 and s.sponsor.userAccount.id=?2")
	Sponsorship findByParade(int paradeId, int sponsorUAId);
}
