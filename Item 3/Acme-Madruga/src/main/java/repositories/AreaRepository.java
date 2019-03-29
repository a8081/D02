
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Area;

public interface AreaRepository extends JpaRepository<Area, Integer> {

	@Query("select b.area from Brotherhood b")
	Collection<Area> AllAreasSettled();

	@Query("select count(b) from Brotherhood b group by area")
	Collection<Integer> getNumberOfBrotherhoodsPerArea();

	@Query("select max(1.0 + (select count(b) from Brotherhood b where b.area.id=a.id) - 1.0), min(1.0 + (select count(b) from Brotherhood b where b.area.id=a.id) - 1.0), avg(1.0 + (select count(b) from Brotherhood b where b.area.id=a.id) - 1.0), stddev(1.0 + (select count(b) from Brotherhood b where b.area.id=a.id) - 1.0) from Area a")
	Double[] getStatiticsBrotherhoodPerArea();

	@Query("select count(ab) from Brotherhood b join b.area ab")
	Integer getNumberOfAreasAssigned();

	@Query("select count(a) from Area a")
	Integer getTotalOfAreas();

	@Query("select sum(case when exists(select b from Brotherhood b where a =b.area) then 1.0 else 0.0 end) / count(a) from Area a")
	Double getRatioBrotherhoodsPerArea();

	/** The ratio of areas that are not co-ordinated by any chapters. */
	@Query("select sum(case when exists(select c from Chapter c where a=c.area) then 0.0 else 1.0 end) / count(a) from Area a")
	Double getRatioNoCoordinatedAreas();

	@Query("select count(a) from Area a where exists(select b from Brotherhood b where a =b.area)")
	Integer getNumberOfAreasWithAnyBrotherhood();

	@Query("select a from Area a where exists(select c from Chapter c where c.area=a) ")
	List<Area> findAreasAssignedToChapter();
}
