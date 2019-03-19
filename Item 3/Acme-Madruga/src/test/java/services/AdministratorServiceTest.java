
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
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	@Autowired
	private AdministratorService	administratorService;


	/* ========================= Test Login Chapter =========================== */
	@Test
	public void driverLoginAdmin() {

		final Object testingData[][] = {
			{
				// Login usuario
				"admin1", null
			}, {
				//Login usuario no registrado
				"AdminNoRegistrado", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateLogin(final String adminUsername, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(adminUsername);
			this.unauthenticate();
			this.administratorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Chapter =========================== */

	@Test
	public void driverCreateAndSaveAdmin() {
		final Object testingData[][] = {
			{
				// Crear admin correctamente
				"admin1", "admin1", "Jose Manuel", "Gonzalez", "conwdasto@jmsx.es", "+34647607406", null
			}, {
				//Crear admin con name incorrecto
				"admin2", "admin2", "Jose Luis", "Gonzalez", "lusi@gamil.es", "mi telefono", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String email, final String phone, final Class<?> expected) {

		Class<?> caught;
		Administrator admin;
		final UserAccount userAccount;

		caught = null;

		try {
			admin = this.administratorService.create();
			admin.setName(name);
			admin.setSurname(surname);
			admin.setEmail(email);
			admin.setPhone(phone);
			userAccount = admin.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			admin.setUserAccount(userAccount);
			admin = this.administratorService.save(admin);
			this.administratorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Edit Chapter =========================== */

	@Test
	public void driverEditAdmin() {

		final Object testingData[][] = {
			{
				// Editar tus datos
				"admin1", "Jose Manuel", "Gonzalez", "conwdasto@jmsx.es", "+34647607406", null
			}, {
				//Editar phone vacio
				"admin1", "Jose Manuel", "Gonzalez", "conwdasto@jmsx.es", "", null
			}, {
				//Editar email incorrecto
				"admin1", "Jose Manuel", "Gonzalez", "no tengo email", "+34647607406", ConstraintViolationException.class
			}, {
				//Editar usuario y dejar nombre en blanco
				"admin1", "", "Gonzalez", "conwdasto@jmsx.es", "+34647607406", ConstraintViolationException.class
			}, {
				//Editar usuario y dejar apellido en blanco
				"admin1", "Jose Manuel", "", "conwdasto@jmsx.es", "+34647607406", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditAdmin((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	private void templateEditAdmin(final String username, final String name, final String surname, final String email, final String phone, final Class<?> expected) {
		Class<?> caught;
		Administrator admin;
		admin = this.administratorService.findOne(this.getEntityId(username));

		caught = null;
		try {
			super.authenticate(username);
			admin.setName(name);
			admin.setSurname(surname);
			admin.setEmail(email);
			admin.setPhone(phone);
			this.unauthenticate();
			this.administratorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);

	}

}
