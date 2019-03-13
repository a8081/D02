
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	@Query("select c from Chapter c where c.userAccount.id=?1")
	Chapter findByUserId(Integer chapterId);

	@Query("select case when (count(a) > 0) then true else false end from Chapter c join c.area a where a.id=?1")
	Boolean hasAnyChapterThisArea(Integer areaId);

	@Query("select c from Chapter c where c.area.id=?1")
	Chapter findChapterByArea(int areaId);

	@Query(value = "SELECT STDDEV(x),MAX(x),MIN(x),AVG(x) FROM (SELECT COUNT(*) AS x FROM `acme-parade`.ENROLMENT WHERE ENROLMENT.drop_out IS NULL GROUP BY brotherhood) AS x", nativeQuery = true)
	Double[] getStatisticsOfMembersPerBrotherhood();

	/** The largest brotherhood is the one with highest number of members **/
	@Query(
		value = "SELECT ENROLMENT.brotherhood FROM `acme-parade`.ENROLMENT LEFT OUTER JOIN BROTHERHOOD ON ENROLMENT.id=BROTHERHOOD.id WHERE ENROLMENT.drop_out IS NULL GROUP BY brotherhood HAVING COUNT(*) = (SELECT MAX(x) FROM (SELECT COUNT(*) AS x FROM `acme-madruga`.ENROLMENT GROUP BY brotherhood)AS T)",
		nativeQuery = true)
	Integer[] getLargestBrotherhood();

	/** The smallest brotherhood is the one with lowest number of members **/
	@Query(
		value = "SELECT ENROLMENT.brotherhood FROM `acme-parade`.ENROLMENT LEFT OUTER JOIN BROTHERHOOD ON ENROLMENT.id=BROTHERHOOD.id WHERE ENROLMENT.drop_out IS NULL GROUP BY brotherhood HAVING COUNT(*) = (SELECT MIN(x) FROM (SELECT COUNT(*) AS x FROM `acme-madruga`.ENROLMENT GROUP BY brotherhood)AS T)",
		nativeQuery = true)
	Integer[] getSmallestBrotherhood();

}
