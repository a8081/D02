
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Folder f join f.messages m where f.id=?1 and f.actor.userAccount.id=?2")
	Collection<Message> findAllByFolderIdAndUserId(Integer mid, Integer uid);

	@Query("select m from Folder f join f.messages m where f.actor.userAccount.id=?1")
	Collection<Message> findAllByUserId(Integer id);

}
