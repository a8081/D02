
package services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class EnrolmentServiceTest extends AbstractTest {

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private EnrolmentService	enrolmentService;


	@Test
	public void getEnrolmentDriver() {
		final Object testingData[][] = {
			{		// Enrole correcto
				"brotherhood2", "member2", null
			}, {	// Enrole con dropOut seteado (no activo) 
				"brotherhood1", "member1", IllegalArgumentException.class
			}, {	// No existe enrole
				"brotherhood1", "member2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.getEnrolmentTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void enroleDriver() {
		final Object testingData[][] = {
			{		// Enrole correcto
				"member2", "brotherhood1", null
			}, {	// Enrole con una brotherhood con la que tengo un enrolment antiguo (drop out != null) 
				"member1", "brotherhood1", null
			}, {	// Enrole con una brotherhood con la que ya tengo enrolment
				"member2", "brotherhood2", IllegalArgumentException.class
			}, {	// Brotherhood no puede establecer enrolments
				"brotherhood1", "brotherhood1", IllegalArgumentException.class
			}, {	// Brotherhood no puede establecer enrolments
				"member1", "member1", IllegalArgumentException.class
			}, {	// Enrolment relaciona a member con brotherhood no a un admin
				"admin1", "brotherhood1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.enroleTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void leaveDriver() {
		final Object testingData[][] = {
			{		// Enrole correcto
				"member2", "brotherhood2", null
			}, {	// Dejar una brotherhood con la que no tengo enrolment
				"member2", "brotherhood1", IllegalArgumentException.class
			}, {	// Dejar una brotherhood que ya deje (dropOut ya seteado anteriormente)
				"member1", "brotherhood1", IllegalArgumentException.class
			}, {	// Brotherhood dejarse a ella misma
				"brotherhood1", "brotherhood1", IllegalArgumentException.class
			}, {	// Member dejarse a el mismo
				"member1", "member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.leaveTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void dropOutDriver() {
		final Object testingData[][] = {
			{		// Drop out enrole correcto
				"brotherhood2", "member2", null
			}, {	// Brotherhood intenta echar a un member que no tiene enrolment con ella
				"brotherhood1", "member2", IllegalArgumentException.class
			}, {	// Brotherhood intenta echar a un member que ya fue echado y por tanto tiene su drop out seteado
				"brotherhood1", "member1", IllegalArgumentException.class
			}, {	// Member intenta echar a otro member
				"member1", "member2", IllegalArgumentException.class
			}, {	// Member intenta echarse a si mismo
				"member1", "member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.dropOutTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final boolean enrolled, final String moment, final String dropOut, final String positionBeanName, final String memberBeanName, final String brotherhoodBeanName, final Class<?> expected) {
		Class<?> caught;
		final Enrolment enrolment;
		Brotherhood brotherhood;
		Member member;
		Date momentDate;
		Date dropOutDate;
		Position position;
		int brotherhoodId;
		int memberId;
		int positionBeanNameId;
		caught = null;
		try {

			this.authenticate(userName);
			brotherhoodId = super.getEntityId(brotherhoodBeanName);
			memberId = super.getEntityId(memberBeanName);
			positionBeanNameId = super.getEntityId(positionBeanName);
			brotherhood = this.brotherhoodService.findOne(brotherhoodId);
			member = this.memberService.findOne(memberId);
			position = this.positionService.findOne(positionBeanNameId);

			enrolment = new Enrolment();
			enrolment.setBrotherhood(brotherhood);
			enrolment.setMember(member);
			enrolment.setEnrolled(enrolled);
			enrolment.setPosition(position);
			if (moment != null)
				momentDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(moment);
			else
				momentDate = null;
			enrolment.setMoment(momentDate);
			if (dropOut != null)
				dropOutDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(dropOut);
			else
				dropOutDate = null;
			enrolment.setDropOut(dropOutDate);

			this.enrolmentService.save(enrolment, brotherhoodId);
			this.enrolmentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void enroleTemplate(final String userName, final String brotherhoodBeanName, final Class<?> expected) {
		Class<?> caught;
		int brotherhoodId;
		caught = null;
		try {

			this.authenticate(userName);
			brotherhoodId = super.getEntityId(brotherhoodBeanName);

			this.enrolmentService.enrole(brotherhoodId);
			this.enrolmentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void leaveTemplate(final String userName, final String brotherhoodBeanName, final Class<?> expected) {
		Class<?> caught;
		Brotherhood brotherhood;
		int brotherhoodId;
		caught = null;
		try {

			this.authenticate(userName);
			brotherhoodId = super.getEntityId(brotherhoodBeanName);
			brotherhood = this.brotherhoodService.findOne(brotherhoodId);

			this.enrolmentService.leave(brotherhood);
			this.enrolmentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void dropOutTemplate(final String userName, final String memberBeanName, final Class<?> expected) {
		Class<?> caught;
		Member member;
		int memberId;
		caught = null;
		try {

			this.authenticate(userName);
			memberId = super.getEntityId(memberBeanName);
			member = this.memberService.findOne(memberId);

			this.enrolmentService.dropOut(member);
			this.enrolmentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void getEnrolmentTemplate(final String brotherhoodBeanName, final String memberBeanName, final Class<?> expected) {
		Class<?> caught;
		Brotherhood brotherhood;
		Member member;
		int brotherhoodId;
		int memberId;
		caught = null;
		try {

			memberId = super.getEntityId(memberBeanName);
			brotherhoodId = super.getEntityId(brotherhoodBeanName);
			brotherhood = this.brotherhoodService.findOne(brotherhoodId);
			member = this.memberService.findOne(memberId);

			this.enrolmentService.getEnrolment(brotherhood, member);
			// this.enrolmentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
