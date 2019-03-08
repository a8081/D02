
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Area;

public interface AreaRepository extends JpaRepository<Area, Integer> {

	@Query("select b.area from Brotherhood b")
	Collection<Area> AllAreasSettled();

	@Query("select count(b) from Brotherhood b group by area")
	Collection<Integer> getNumberOfBrotherhoodsPerArea();

	@Query(value = "SELECT MAX(x),MIN(x),AVG(x),STDDEV(x) FROM (SELECT COUNT(*) AS x FROM `acme-madruga`.BROTHERHOOD GROUP BY area) AS x", nativeQuery = true)
	Double[] getStatiticsBrotherhoodPerArea();

	@Query("select count(ab) from Brotherhood b join b.area ab")
	Integer getNumberOfAreasAssigned();

	@Query("select count(a) from Area a")
	Integer getTotalOfAreas();

	@Query("select sum(case when exists(select b from Brotherhood b where a =b.area) then 1.0 else 0.0 end) / count(a) from Area a")
	Double getRatioBrotherhoodsPerArea();

	@Query("select count(a) from Area a where exists(select b from Brotherhood b where a =b.area)")
	Integer getNumberOfAreasWithAnyBrotherhood();
}
