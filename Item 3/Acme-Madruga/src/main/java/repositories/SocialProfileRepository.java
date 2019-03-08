
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.SocialProfile;

@Repository
public interface SocialProfileRepository extends JpaRepository<SocialProfile, Integer> {

	//@Query("select a from Actor a where a.userAccount.id=?1")
	//SocialProfile findByUserId(Integer id);

	@Query("select s from SocialProfile s where s.actor.id=?1")
	Collection<SocialProfile> findByActorId(Integer id);
}
