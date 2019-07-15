
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
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
	public void leaveDriver() {
		final Object testingData[][] = {
			{		// Enrole correcto
				"member2", "brotherhood2", null
			}, {	// Dejar una brotherhood con la que no tengo enrolment
				"member2", "brotherhood1", IllegalArgumentException.class
			},
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

}
