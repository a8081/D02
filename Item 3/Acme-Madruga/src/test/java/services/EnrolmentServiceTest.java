
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


	//	@Test
	//	public void createAndSaveDriver() {
	//		final Object testingData[][] = {
	//			{	//Creacion correcta de un request
	//				"member1", "service2", "rendezvous3", "one comment", "holder", "brand", "4539433728995809", 10, 20, 150, null
	//			}, {	//Creacion correcta de un request sin comentario
	//				"member1", "service2", "rendezvous3", null, "holder", "brand", "4539433728995809", 10, 20, 150, null
	//			}, {	//Creacion correcta de un request con comentario blanco
	//				"member1", "service2", "rendezvous3", "", "holder", "brand", "4539433728995809", 10, 20, 150, null
	//			}, {	//Request sin user logueado
	//				null, "service2", "rendezvous3", "one commnent", "holder", "brand", "4539433728995809", 10, 20, 150, IllegalArgumentException.class
	//			}, {	//Request con holdername vacio
	//				"member1", "service2", "rendezvous3", "one comment", "", "brand", "4539433728995809", 10, 20, 150, ConstraintViolationException.class
	//			}, {	//Request con holdername nulo
	//				"member1", "service2", "rendezvous3", "one comment", null, "brand", "4539433728995809", 10, 20, 150, ConstraintViolationException.class
	//			}, {	//Request con brandname vacio
	//				"member1", "service2", "rendezvous3", "one comment", "holder", "", "4539433728995809", 10, 20, 150, ConstraintViolationException.class
	//			}, {	//Request con brandname nulo
	//				"member1", "service2", "rendezvous3", "one comment", "holder", null, "4539433728995809", 10, 20, 150, ConstraintViolationException.class
	//			}
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			try {
	//				super.startTransaction();
	//				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
	//					(int) testingData[i][7], (int) testingData[i][8], (int) testingData[i][9], (Class<?>) testingData[i][10]);
	//			} catch (final Throwable oops) {
	//				throw new RuntimeException(oops);
	//			} finally {
	//				super.rollbackTransaction();
	//			}
	//	}

	@Test
	public void enroleDriver() {
		final Object testingData[][] = {
			{		// Enrole correcto
				"member2", "Esperanza de triana", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.enroleTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][10]);
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
		Brotherhood member;
		int memberId;
		caught = null;
		try {

			this.authenticate(userName);
			memberId = super.getEntityId(memberBeanName);
			member = this.brotherhoodService.findOne(memberId);

			this.enrolmentService.leave(member);
			this.enrolmentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
