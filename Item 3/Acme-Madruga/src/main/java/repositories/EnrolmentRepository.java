
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {

	@Query("select e from Enrolment e where e.member.userAccount.id=?1")
	Collection<Enrolment> findAllByMemberId(Integer memberUAId);

	@Query("select e from Enrolment e where e.brotherhood.userAccount.id=?1")
	Collection<Enrolment> findAllByBrotherHoodId(Integer broUAId);

	@Query("select e from Enrolment e where e.brotherhood.userAccount.id=?1 and e.member.userAccount.id=?2")
	Collection<Enrolment> findEnrolmentFromBroMember(Integer broUAId, Integer memberUAId);

}
