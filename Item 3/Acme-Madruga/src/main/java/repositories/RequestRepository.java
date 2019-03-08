
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

	@Query("select case when (count(r) > 1) then true else false end from Request r where r.procession.id=?1 and r.status='APPROVED'")
	Boolean processionRequested(Integer processionId);

	@Query("select count(r) from Request r where r.status='APPROVED'")
	Integer totalApprovedRequest();

	@Query("select r from Request r where r.status='APPROVED' and r.procession.brotherhood.userAccount.id = ?1")
	Collection<Request> findApprovedBrotherhood(Integer brotherhoodUserAccountId);

	@Query("select r from Request r where r.status='REJECTED' and r.procession.brotherhood.userAccount.id = ?1")
	Collection<Request> findRejectedBrotherhood(Integer brotherhoodUserAccountId);

	@Query("select r from Request r where r.status='PENDING' and r.procession.brotherhood.userAccount.id = ?1")
	Collection<Request> findPendingBrotherhood(Integer brotherhoodUserAccountId);

	@Query("select r from Request r where r.procession.id = ?1")
	Collection<Request> findByProcesion(Integer idProcesion);

	@Query("select r from Request r join r.procession p where p.brotherhood.userAccount.id = ?1")
	Collection<Request> findByBrotherhood(Integer idBrotherhood);

	@Query("select case when (count(r) > 0) then true else false end from Request r join r.procession p where (p.brotherhood.userAccount.id = ?1 and r.id = ?2)")
	Boolean checkBrotherhoodAccess(Integer idBrotherhood, Integer idRequest);

	@Query("select case when (count(r) > 0) then true else false end from Request r where (r.procession.id = ?1 and r.member.userAccount.id = ?2)")
	Boolean hasMemberRequestToProcession(Integer processionId, Integer memberUserAccountId);

	@Query("select case when (count(r)=0) then true else false end from Request r where r.row=?1 and r.column=?2 and r.procession.id=?3")
	Boolean availableRowColumn(Integer rowNumber, Integer columnNumber, Integer idProcession);

	@Query("select sum(case when r.status='PENDING' then 1.0 else 0.0 end) / count(r) from Request r")
	Double findPendingRequestRatio();

	@Query("select sum(case when r.status='APPROVED' then 1.0 else 0.0 end) / count(r) from Request r")
	Double findApprovedRequestRatio();

	@Query("select sum(case when r.status='REJECTED' then 1.0 else 0.0 end) / count(r) from Request r")
	Double findRejectedRequestRatio();

	@Query("select sum(case when r.status='REJECTED' then 1.0 else 0.0 end) / count(r) from Request r where r.procession.id=?1")
	Double findRejectedRequestByProcessionRatio(Integer processionId);

	@Query("select sum(case when r.status='PENDING' then 1.0 else 0.0 end) / count(r) from Request r where r.procession.id=?1")
	Double findPendingRequestByProcessionRatio(Integer processionId);

	@Query("select sum(case when r.status='APPROVED' then 1.0 else 0.0 end) / count(r) from Request r where r.procession.id=?1")
	Double findApprovedRequestByProcessionRatio(Integer processionId);

}
