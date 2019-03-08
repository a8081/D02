
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select e.position from Enrolment e")
	Collection<Position> AllPositionUsed();

	@Query("select e.position, count(e) from Enrolment e group by position")
	List<Object[]> getPositionFrequency();
}
