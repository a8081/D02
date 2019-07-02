
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Sponsor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorServiceTest extends AbstractTest {

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private UserAccountService	userAccountService;


	/* ========================= Test Create and Save Sponsor =========================== */

	@Test
	public void driverCreateAndSaveSponsor() {
		final Object testingData[][] = {
			{
				// Crear sponsor correctamente
				"sponsortest", "sponsortest", "sponsortest", "surnameSponsor", "sponsortest@hotmail.es", "+34655398741", null
			}, {
				//Crear sponsor con telefono incorrecto
				"sponsortest", "sponsortest", "sponsortest", "surnameSponsor", "sponsortest@hotmail.es", "mi telefono", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String email, final String phone, final Class<?> expected) {

		Class<?> caught;
		final Sponsor sponsor;
		caught = null;

		try {
			sponsor = this.sponsorService.create();
			sponsor.setName(name);
			sponsor.setSurname(surname);
			sponsor.setEmail(email);
			sponsor.setPhone(phone);
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.SPONSOR);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(username);
			account.setPassword(password);
			sponsor.setUserAccount(account);
			this.sponsorService.save(sponsor);
			this.sponsorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}
}
