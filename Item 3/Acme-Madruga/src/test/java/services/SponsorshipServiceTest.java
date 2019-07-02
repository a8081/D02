
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Sponsorship;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	@Autowired
	private SponsorshipService	sponsorshipService;


	/* ========================= Test Create Segment =========================== */

	@Test
	public void driverEditSponsorship() {

		final Object testingData[][] = {
			{
				//Editar sponsorship correctamente
				"sponsor1", "sponsorship1", "https://ghc.anitabtest.org", null
			}, {
				//Editar sponsorship con target erroneo
				"sponsor1", "sponsorship1", "target", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSponsorship((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateCreateSponsorship(final String sponsor, final String sponsorshipName, final String target, final Class<?> expected) {

		Class<?> caught;
		final Sponsorship sponsorship;
		caught = null;

		try {
			this.authenticate(sponsor);
			sponsorship = this.sponsorshipService.findOne(super.getEntityId(sponsorshipName));
			sponsorship.setTargetPage(target);
			this.sponsorshipService.save(sponsorship);
			this.sponsorshipService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);

	}
}
