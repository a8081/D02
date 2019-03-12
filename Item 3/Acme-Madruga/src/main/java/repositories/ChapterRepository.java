
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	@Query("select c from Chapter c where c.userAccount.id=?1")
	Chapter findByUserId(Integer chapterId);

	@Query("select case when (count(a) > 0) then true else false end from Chapter c join c.area a where a.id=?1")
	Boolean hasAnyChapterThisArea(Integer areaId);

	@Query("select c from Chapter c where c.area.id=?1")
	Chapter findChapterByArea(int areaId);

}
