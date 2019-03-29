
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.Parade;
import domain.Segment;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Integer> {

	@Query("select p.brotherhood from Parade p join p.segments s where s.id=?1")
	Brotherhood findBrotherhoodBySegment(int id);

	@Query("select p from Parade p join p.segments s where s.id=?1")
	Parade findParadeBySegment(int id);

	@Query("select s from Parade p join p.segments s where p.id=?1")
	List<Segment> findSegmentsByParade(int id);
}
