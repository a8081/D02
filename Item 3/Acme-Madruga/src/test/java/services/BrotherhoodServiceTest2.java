
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BrotherhoodServiceTest2 extends AbstractTest {

	@Autowired
	private BrotherhoodService	brotherhoodService;


	@Test
	public void driverLoginBrotherhood() {

		final Object testingData[][] = {
			{
				// Login usuario
				"brotherhood1", null
			}, {
				//Login usuario no registrado
				"BrotherhoodNoRegistrado", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateLogin(final String brotherhoodUsername, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(brotherhoodUsername);
			this.unauthenticate();
			this.brotherhoodService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Brotherhoood =========================== */

	@Test
	public void driverCreateAndSaveBrotherhood() {
		final Object testingData[][] = {
			{
				// Crear brotherhood correctamente
				"brotherhood1", "brotherhood1", "Esperanza de triana", "surnameBrother", "esperanza@hotmail.es", "+34655398741", "La Esperanza de Triana", null
			}, {
				//Crear manager con name incorrecto
				"brotherhood1", "brotherhood1", "Esperanza de triana", "surnameBrother", "esperanza@hotmail.es", "mi telefono", "La Esperanza de Triana", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(Class<?>) testingData[i][8]);
	}

	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String email, final String phone, final String title, final Class<?> expected) {

		Class<?> caught;
		final Brotherhood brotherhood;
		final UserAccount userAccount;

		caught = null;

		try {
			brotherhood = this.brotherhoodService.create();
			brotherhood.setName(name);
			brotherhood.setSurname(surname);
			brotherhood.setEmail(email);
			brotherhood.setPhone(phone);
			brotherhood.setTitle(title);
			userAccount = brotherhood.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			brotherhood.setUserAccount(userAccount);
			this.brotherhoodService.save(brotherhood);
			this.brotherhoodService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Edit Brotherhood =========================== */

	@Test
	public void driverEditBrotherhood() {

		final Object testingData[][] = {
			{
				// Editar tus datos
				"brotherhood1", "brotherhood1", "Esperanza de triana", "surnameBrother", "esperanza@hotmail.es", "+34655398741", "La Esperanza de Triana", null
			}, {
				//Editar phone vacio
				"brotherhood1", "brotherhood1", "Esperanza de triana", "surnameBrother", "esperanza@hotmail.es", "", "La Esperanza de Triana", ConstraintViolationException.class
			}, {
				//Editar email incorrecto
				"brotherhood1", "brotherhood1", "Esperanza de triana", "surnameBrother", "esperanza1@hotmail.es", "+34655398741", "La Esperanza de Triana", ConstraintViolationException.class
			}, {
				//Editar usuario y dejar nombre en blanco
				"brotherhood1", "brotherhood1", "", "surnameBrother", "esperanza@hotmail.es", "+34655398741", "La Esperanza de Triana", ConstraintViolationException.class
			}, {
				//Editar usuario y dejar apellido en blanco
				"brotherhood1", "brotherhood1", "Esperanza de triana", "", "esperanza@hotmail.es", "+34655398741", "La Esperanza de Triana", ConstraintViolationException.class
			}, {
				//Editar usuario y dejar title en blanco
				"brotherhood1", "brotherhood1", "Esperanza de triana", "surnameBrother", "esperanza@hotmail.es", "+34655398741", "", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditBrotherhood((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateEditBrotherhood(final String username, final String name, final String surname, final String email, final String phone, final String title, final Class<?> expected) {
		Class<?> caught;
		Brotherhood brotherhood;
		brotherhood = this.brotherhoodService.findOne(this.getEntityId(username));

		caught = null;
		try {
			super.authenticate(username);
			brotherhood.setName(name);
			brotherhood.setSurname(surname);
			brotherhood.setEmail(email);
			brotherhood.setPhone(phone);
			brotherhood.setTitle(title);
			this.unauthenticate();
			this.brotherhoodService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);

	}
}
