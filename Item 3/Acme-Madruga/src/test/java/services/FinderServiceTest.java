
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

import utilities.AbstractTest;
import domain.Finder;
import domain.Parade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private FinderService	finderService;


	/* ========================= Test Create and Save Brotherhoood =========================== */

	@Test
	public void driverCreateAndSaveFinder() {
		final Collection<Parade> parades = new ArrayList<>();
		final Object testingData[][] = {
			{
				// Crear chapter correctamente
				"member1", "Esperanza de triana", "Capilla San Jorge", parades, null
			}, {
				//Crear manager con name incorrecto
				"member1", "", "Capilla San Jorge", parades, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<Parade>) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	private void templateCreateAndSave(final String username, final String keyword, final String areaName, final Collection<Parade> parades, final Class<?> expected) {

		Class<?> caught;
		final Finder finder;

		caught = null;

		try {
			this.authenticate(username);
			finder = this.finderService.create();
			finder.setKeyword(keyword);
			finder.setAreaName(areaName);
			finder.setParades(parades);
			this.finderService.save(finder);
			this.finderService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

}
