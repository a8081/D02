
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

	/** The average, the minimum, the maximum and the standard deviation of the number of records per history */
	@Query("select avg(1.0 + periodRecords.size+legalRecords.size+linkRecords.size+miscellaneousRecords.size), min(1.0 + periodRecords.size+legalRecords.size+linkRecords.size+miscellaneousRecords.size), max(1.0 + periodRecords.size+legalRecords.size+linkRecords.size+miscellaneousRecords.size), stddev(1.0 + periodRecords.size+legalRecords.size+linkRecords.size+miscellaneousRecords.size) from History h")
	Double[] getStatisticsOfRecordsPerHistory();

	/** The largest brotherhood is the one with highest number of records per history **/
	@Query("select b from Brotherhood b where (b.history.periodRecords.size+b.history.legalRecords.size+b.history.linkRecords.size+b.history.miscellaneousRecords.size)=(select max(p.history.periodRecords.size+p.history.legalRecords.size+p.history.linkRecords.size+p.history.miscellaneousRecords.size) from Brotherhood p)")
	Collection<Brotherhood> getLargestBrotherhoodPerHistory();

	/** The smallest brotherhood is the one with lowest number of records per history **/
	@Query("select b from Brotherhood b where (b.history.periodRecords.size+b.history.legalRecords.size+b.history.linkRecords.size+b.history.miscellaneousRecords.size)>(select stddev(p.history.periodRecords.size+p.history.legalRecords.size+p.history.linkRecords.size+p.history.miscellaneousRecords.size) from Brotherhood p)")
	Brotherhood[] getBrotherhoodPerHistoryLargerThanStd();

}
