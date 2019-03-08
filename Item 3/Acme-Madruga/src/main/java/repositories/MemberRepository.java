
package repositories;

import java.util.Collection;
import java.util.List;

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

	/**
	 * Devuelve el listado de miembros que cumplen que el número de requests aceptadas solicitadas
	 * es al menos el 10% del número de solicitudes aceptadas que tiene el miembro con
	 * el maximo numero de solicitudes aceptadas
	 **/
	@Query(
		value = "SELECT MEMBER FROM `acme-madruga`.REQUEST WHERE status='ACCEPTED' GROUP BY procession HAVING COUNT(*) >= 0.1*(SELECT MAX(x) FROM (SELECT COUNT(*) AS x FROM `acme-madruga`.REQUEST WHERE REQUEST.status='ACCEPTED'  GROUP BY procession)AS X)",
		nativeQuery = true)
	List<Member> getMembersTenPercent();
}
