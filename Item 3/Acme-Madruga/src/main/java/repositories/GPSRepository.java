
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.GPS;
import domain.Segment;

@Repository
public interface GPSRepository extends JpaRepository<GPS, Integer> {

	@Query("select p.brotherhood from Parade p join p.segments s join s.gps g where g.id=?1")
	Brotherhood findBrotherhoodByGPS(int idGPS);

	@Query("select s from Segment s join s.gps g where g.id=?1")
	Segment findSegmentByGPS(int id);

}
