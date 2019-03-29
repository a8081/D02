
package repositories;

import java.util.Collection;

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

	/** The average, the minimum, the maximum and the standard deviation of the number of parades co-ordinated by the chapters */
	@Query("select avg(1.0 + (select count(p) from Parade p join p.brotherhood.area a where c.area.id=a.id)-1.0), min(1.0 + (select count(p) from Parade p join p.brotherhood.area a where c.area.id=a.id)-1.0), max(1.0 + (select count(p) from Parade p join p.brotherhood.area a where c.area.id=a.id)-1.0), stddev(1.0 + (select count(p) from Parade p join p.brotherhood.area a where c.area.id=a.id)-1.0) from Chapter c)")
	Double[] getStatisticsOfParadesPerChapter();

	/** The chapters that co-ordinate at least 10 per cent more parades than de average */
	@Query("select distinct c from Chapter c where (1.0*(select count(p) from Parade p join p.brotherhood.area a where c.area.id=a.id))>=(1.1*(select avg(1.0+(select count(h) from Parade h join h.brotherhood.area r where k.area.id=r.id)-1.0) from Chapter k))")
	Collection<Chapter> findTenPerCentMoreParadesThanAverage();

}
