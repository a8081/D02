
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Request r join r.member m where m.userAccount.id = ?1")
	Collection<Request> findByMember(Integer idMember);

	@Query("select case when (count(r) > 1) then true else false end from Request r where r.parade.id=?1 and r.status='APPROVED'")
	Boolean paradeRequested(Integer paradeId);

	@Query("select count(r) from Request r where r.status='APPROVED'")
	Integer totalApprovedRequest();

	@Query("select r from Request r where r.status='APPROVED' and r.parade.brotherhood.userAccount.id = ?1")
	Collection<Request> findApprovedBrotherhood(Integer brotherhoodUserAccountId);

	@Query("select r from Request r where r.status='REJECTED' and r.parade.brotherhood.userAccount.id = ?1")
	Collection<Request> findRejectedBrotherhood(Integer brotherhoodUserAccountId);

	@Query("select r from Request r where r.status='PENDING' and r.parade.brotherhood.userAccount.id = ?1")
	Collection<Request> findPendingBrotherhood(Integer brotherhoodUserAccountId);

	@Query("select r from Request r where r.parade.id = ?1")
	Collection<Request> findByProcesion(Integer idProcesion);

	@Query("select r from Request r join r.parade p where p.brotherhood.userAccount.id = ?1")
	Collection<Request> findByBrotherhood(Integer idBrotherhood);

	@Query("select case when (count(r) > 0) then true else false end from Request r join r.parade p where (p.brotherhood.userAccount.id = ?1 and r.id = ?2)")
	Boolean checkBrotherhoodAccess(Integer idBrotherhood, Integer idRequest);

	@Query("select case when (count(r) > 0) then true else false end from Request r where (r.parade.id = ?1 and r.member.userAccount.id = ?2)")
	Boolean hasMemberRequestToParade(Integer paradeId, Integer memberUserAccountId);

	@Query("select case when (count(r)=0) then true else false end from Request r where r.row=?1 and r.column=?2 and r.parade.id=?3")
	Boolean availableRowColumn(Integer rowNumber, Integer columnNumber, int idParade);

	@Query("select count(r) from Request r where r.row=?1 and r.column=?2 and r.parade.id=?3")
	Integer existsRowColumn(Integer rowNumber, Integer columnNumber, int idParade);

	@Query("select sum(case when r.status='PENDING' then 1.0 else 0.0 end) / count(r) from Request r")
	Double findPendingRequestRatio();

	@Query("select sum(case when r.status='APPROVED' then 1.0 else 0.0 end) / count(r) from Request r")
	Double findApprovedRequestRatio();

	@Query("select sum(case when r.status='REJECTED' then 1.0 else 0.0 end) / count(r) from Request r")
	Double findRejectedRequestRatio();

	@Query("select sum(case when r.status='REJECTED' then 1.0 else 0.0 end) / count(r) from Request r where r.parade.id=?1")
	Double findRejectedRequestByParadeRatio(Integer paradeId);

	@Query("select sum(case when r.status='PENDING' then 1.0 else 0.0 end) / count(r) from Request r where r.parade.id=?1")
	Double findPendingRequestByParadeRatio(Integer paradeId);

	@Query("select sum(case when r.status='APPROVED' then 1.0 else 0.0 end) / count(r) from Request r where r.parade.id=?1")
	Double findApprovedRequestByParadeRatio(Integer paradeId);

}
