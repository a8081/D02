
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.GPS;

@Repository
public interface GPSRepository extends JpaRepository<GPS, Integer> {

}
