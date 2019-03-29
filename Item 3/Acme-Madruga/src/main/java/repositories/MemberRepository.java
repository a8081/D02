
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("select m from Member m where m.userAccount.id=?1")
	Member findByUserId(Integer memberId);

	@Query("select e.member from Enrolment e where e.brotherhood.userAccount.id=?1 and e.dropOut=null")
	Collection<Member> allMembersFromBrotherhood(Integer broUAId);

	@Query("select req.member from Request req where req.id=?1")
	Member findByRequestId(int requestId);

	@Query("select e.member from Enrolment e where e.id=?1")
	Member findByEnrolmentId(int enrolmentId);

	/**
	 * Devuelve el listado de miembros que cumplen que el número de requests aceptadas solicitadas
	 * es al menos el 10% del número de solicitudes aceptadas que tiene el miembro con
	 * el maximo numero de solicitudes aceptadas
	 **/
	@Query("select distinct mt from Member mt where (1.0+(select count(r) from Request r where r.status='APPROVED' and r.member.id=mt.id)-1.0)=(select max(1.0+(select count(r) from Request r where r.status='APPROVED' and r.member.id=mk.id)-1.0) from Member mk)")
	Member[] getMembersTenPercent();

}
