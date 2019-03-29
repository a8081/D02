
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Float;
import domain.Parade;

@Repository
public interface FloatRepository extends JpaRepository<Float, Integer> {

	@Query("select f from Float f where f.brotherhood.userAccount.id = ?1")
	Collection<Float> findByBrotherhood(int brotherhooodUserAccountId);

	@Query("select p from Parade p join p.floats b where b.title =?1")
	List<Parade> findForFloat(String fProcession);

}
