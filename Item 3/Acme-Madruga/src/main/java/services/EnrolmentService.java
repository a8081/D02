
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EnrolmentRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import forms.EnrolmentForm;

@Service
@Transactional
public class EnrolmentService {

	@Autowired
	private EnrolmentRepository	enrolmentRepository;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private Validator			validator;


	public Enrolment create() {
		final Enrolment enrolment = new Enrolment();
		this.memberService.findByPrincipal();
		return enrolment;

	}

	public Collection<Enrolment> findAll() {
		Collection<Enrolment> res = new ArrayList<>();
		final Actor principal = this.actorService.findByPrincipal();
		final Boolean isBrotherhood = this.actorService.checkAuthority(principal, Authority.BROTHERHOOD);
		final Boolean isMember = this.actorService.checkAuthority(principal, Authority.MEMBER);

		if (isBrotherhood)
			res = this.enrolmentRepository.findAllByBrotherHoodId(principal.getUserAccount().getId());
		else if (isMember)
			res = this.enrolmentRepository.findAllByMemberId(principal.getUserAccount().getId());
		//Si salta puede ser un Admin
		Assert.notNull(res);
		return res;
	}

	public Enrolment findOne(final int enrolmentId) {
		Assert.isTrue(enrolmentId != 0);
		final Enrolment res = this.enrolmentRepository.findOne(enrolmentId);
		Assert.notNull(res);
		return res;
	}

	public Enrolment save(final Enrolment enrolment, final int brotherhoodId) {
		Assert.notNull(enrolment);
		Assert.isTrue(brotherhoodId != 0);
		final Actor principal = this.actorService.findByPrincipal();
		final Enrolment result;
		final Boolean isMember = this.actorService.checkAuthority(principal, Authority.MEMBER);

		if (isMember) {
			if (enrolment.getId() == 0) {
				Assert.isTrue(!this.brotherhoodService.findAllMyBrotherHoodByMember().contains(enrolment.getBrotherhood()));
				final Date moment = new Date(System.currentTimeMillis() - 1);
				enrolment.setMoment(moment);
				enrolment.setEnrolled(false);
				enrolment.setMember(this.memberService.findByPrincipal());
				enrolment.setDropOut(null);
				enrolment.setBrotherhood(this.brotherhoodService.findOne(brotherhoodId));
			} else
				Assert.isTrue(enrolment.getMember() == this.memberService.findByPrincipal());
			if (enrolment.getDropOut() != null)
				Assert.isTrue(enrolment.getMoment().before(enrolment.getDropOut()));

		} else { // BROTHERHOOD
			Assert.isTrue(enrolment.getBrotherhood() == this.brotherhoodService.findByPrincipal());
			Assert.notNull(enrolment.getPosition());
			enrolment.setEnrolled(true);
			if (enrolment.getDropOut() != null)
				Assert.isTrue(enrolment.getMoment().before(enrolment.getDropOut()));
		}
		result = this.enrolmentRepository.save(enrolment);
		return result;
	}
	public void delete(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);
		Assert.isTrue(this.enrolmentRepository.exists(enrolment.getId()));
		this.enrolmentRepository.delete(enrolment);
	}

	/* ========================= OTHER METHODS =========================== */

	/* Una hermandad elimina a un miembro */
	public void dropOut(final Member member) {
		Assert.notNull(member);
		Assert.isTrue(this.memberService.findAll().contains(member));
		final Actor principal = this.actorService.findByPrincipal();
		final Boolean isBrotherhood = this.actorService.checkAuthority(principal, Authority.BROTHERHOOD);
		Collection<Enrolment> enrolments;
		Enrolment enrolment;

		if (isBrotherhood) {
			enrolments = this.enrolmentRepository.findEnrolmentFromBroMember(principal.getUserAccount().getId(), member.getUserAccount().getId());
			Assert.notNull(enrolments);
			enrolment = this.enrolmentActive(enrolments);
			Assert.notNull(enrolment, "No puede expulsar de la hermandad a un miembro que no pertenece a ella.");
			enrolment.setDropOut(new Date(System.currentTimeMillis() - 1));
		}
	}

	/* Un miembro sale de una hermandad */
	public void leave(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		Assert.isTrue(this.brotherhoodService.findAll().contains(brotherhood));
		final Actor principal = this.actorService.findByPrincipal();
		final Boolean isMember = this.actorService.checkAuthority(principal, Authority.MEMBER);
		Assert.isTrue(this.brotherhoodService.findAllMyBrotherHoodByMember().contains(brotherhood), "No puede darse de baja de una hermandad a la que no pertenece.");
		Collection<Enrolment> enrolments;
		Enrolment enrolment;

		if (isMember) {
			enrolments = this.enrolmentRepository.findEnrolmentFromBroMember(brotherhood.getUserAccount().getId(), principal.getUserAccount().getId());
			Assert.notNull(enrolments);
			enrolment = this.enrolmentActive(enrolments);
			Assert.notNull(enrolment, "No puede darse de baja de una hermandad a la que no pertenece.");
			enrolment.setDropOut(new Date(System.currentTimeMillis() - 1));
		}
	}

	public Enrolment getEnrolment(final Actor brotherhood, final Actor member) {
		final Collection<Enrolment> res = this.enrolmentRepository.findEnrolmentFromBroMember(brotherhood.getUserAccount().getId(), member.getUserAccount().getId());
		final Enrolment enrolment = this.enrolmentActive(res);
		Assert.notNull(res);
		//		Assert.notNull(enrolment);
		//		Assert.notNull(enrolment.getMoment());

		return enrolment;
	}

	public Collection<Enrolment> findAllByMemberId(final Integer memberUAId) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.MEMBER));
		final Collection<Enrolment> res = this.enrolmentRepository.findAllByMemberId(memberUAId);
		return res;
	}

	public Collection<Enrolment> findAllByBrotherHoodId(final Integer broUAId) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.BROTHERHOOD));
		final Collection<Enrolment> res = this.enrolmentRepository.findAllByBrotherHoodId(broUAId);
		return res;
	}

	public Enrolment enrolmentActive(final Collection<Enrolment> enrolments) {
		Enrolment enrolment = null;
		for (final Enrolment e : enrolments)
			if (e.getDropOut() == null) {
				enrolment = e;
				break;
			}
		return enrolment;
	}

	public Enrolment reconstruct(final EnrolmentForm enrolmentForm, final BindingResult binding) {
		Enrolment result;

		Assert.isTrue(enrolmentForm.getId() != 0);

		result = this.findOne(enrolmentForm.getId());

		result.setId(enrolmentForm.getId());
		result.setVersion(enrolmentForm.getVersion());
		result.setPosition(enrolmentForm.getPosition());

		this.validator.validate(result, binding);

		return result;
	}

	public Enrolment enrole(final int brotherhoodId) {
		final Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		final Member member = this.memberService.findByPrincipal();
		Assert.notNull(brotherhood.getArea(), "No se puede inscribir en una hermandad que no tiene área seleccionada.");
		Assert.isNull(this.getEnrolment(brotherhood, member), "No puedes inscribirte más de una vez en la misma hermandad.");
		final Enrolment enrolment = this.create();

		final Enrolment retrieved = this.save(enrolment, brotherhoodId);

		return retrieved;
	}

}
