
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Proclaim;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ProclaimServiceTest extends AbstractTest {

	// Services
	@Autowired
	private ProclaimService	proclaimService;


	@Test
	public void driverCreateSave() {
		final Object testingData[][] = {
			{
				//Correcto
				"chapter1", "descriptionTest", null
			},
			{
				//Crear proclaim con usuario null
				null, "descriptionTest", IllegalArgumentException.class
			},
			{
				//Crear proclaim con usuairo no chapter
				"member1", "descriptionTest", IllegalArgumentException.class
			},
			{
				//Crear proclaim con cadena vacia
				"chapter1", "", IllegalArgumentException.class
			},
			{
				//Crear proclaim con cadena >250
				"chapter1",
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
					+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", IllegalArgumentException.class
			}, {
				//Crear proclaim con text null
				"chapter1", null, IllegalArgumentException.class
			}, {
				//Crear proclaim con cadena vacia
				"chapter1", "edit", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateCreateSave(final String user, final String text, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final Proclaim proc = this.proclaimService.create();
			proc.setText(text);
			final Proclaim procSaved = this.proclaimService.save(proc);
			Assert.isTrue(procSaved.getId() != 0);
			if (text.equals("edit"))
				this.proclaimService.save(procSaved);
			this.proclaimService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
