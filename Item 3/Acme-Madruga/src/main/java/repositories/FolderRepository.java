
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

	@Query("select f from Folder f where f.actor.userAccount.id = ?1 and f.name='In box'")
	Folder findInboxByUserId(Integer id);

	@Query("select f from Folder f where f.actor.userAccount.id = ?1 and f.name='Out box'")
	Folder findOutboxByUserId(Integer id);

	@Query("select f from Folder f where f.actor.userAccount.id = ?1 and f.name='Spam box'")
	Folder findSpamboxByUserId(Integer id);

	@Query("select f from Folder f where f.actor.userAccount.id = ?1 and f.name='Trash box'")
	Folder findTrashboxByUserId(Integer id);

	@Query("select f from Folder f where f.actor.userAccount.id = ?1 and f.name='Notification box'")
	Folder findNotificationboxByUserId(Integer id);

	@Query("select f from Folder f join f.messages m where f.actor.userAccount.id=?1 and m.id=?2")
	Collection<Folder> findAllByMessageIdAndUserId(Integer uid, Integer mid);

	@Query("select f from Folder f where f.actor.userAccount.id=?1")
	Collection<Folder> findAllByUserId(Integer id);

	@Query("select f from Folder f where f.father.id=?1")
	Collection<Folder> findAllByFatherId(Integer id);

	@Query("select f from Folder f where f.isSystemFolder = true and f.actor.userAccount.id=?1")
	Collection<Folder> findAllSystemFolderByUserId(Integer id);

	@Query("select f from Folder f where f.father.id = null and f.actor.userAccount.id=?1")
	Collection<Folder> findAllFolderFatherNullByUserId(Integer id);

	/*
	 * @Query(
	 * value = "SELECT MEMBER FROM `acme-madruga`.REQUEST WHERE status='ACCEPTED' GROUP BY procession HAVING COUNT(*) >= 0.1*(SELECT MAX(x) FROM (SELECT COUNT(*) AS x FROM `acme-madruga`.REQUEST WHERE REQUEST.status='ACCEPTED'  GROUP BY procession)AS X)",
	 * nativeQuery = true)
	 * List<domain.Member> getMembersTenPercentMaxRequestAccepted();
	 */

}
