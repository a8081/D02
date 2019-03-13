
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Chapter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChapterServiceTest extends AbstractTest {

	@Autowired
	private ChapterService	chapterService;


	/* ========================= Test Login Chapter =========================== */
	@Test
	public void driverLoginChapter() {

		final Object testingData[][] = {
			{
				// Login usuario
				"chapter1", null
			}, {
				//Login usuario no registrado
				"ChapterNoRegistrado", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateLogin(final String chapterUsername, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(chapterUsername);
			this.unauthenticate();
			//			this.chapterService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Chapter =========================== */

	@Test
	public void driverCreateAndSaveChapter() {
		final Object testingData[][] = {
			{
				// Crear chapter correctamente
				"chapterTest1", "chapterTest1", "nameChapterTest1", "surnameChapterTest1", "titleChapterTest1", null
			}, {
				//Crear manager con name incorrecto
				"chapterTest1", "chapterTest1", " ", "surnameChapterTest1", "titleChapterTest1", javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String email, final String phone, final String title, final Class<?> expected) {

		Class<?> caught;
		Chapter chapter;
		final UserAccount userAccount;

		caught = null;

		try {
			chapter = this.chapterService.create();
			chapter.setName(name);
			chapter.setSurname(surname);
			chapter.setEmail(email);
			chapter.setPhone(phone);
			chapter.setTitle(title);
			userAccount = chapter.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			chapter.setUserAccount(userAccount);
			chapter = this.chapterService.save(chapter);
			//			this.chapterService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Edit Chapter =========================== */

	@Test
	public void driverEditChapter() {

		final Object testingData[][] = {
			{
				// Editar tus datos
				"chapter1", "Jesus", "Garcia", "jesus@mail.com", "956657894", "titleJesus", null
			}, {
				//Editar phone incorrecto
				"chapter1", "Jesus", "Garcia", "jesus@mail.com", "telefono", "titleJesus", null
			}, {
				//Editar tu email en blanco
				"chapter1", "Jesus", "Garcia", "", "956657894", "titleJesus", null
			}, {
				//Editar email incorrecto
				"chapter1", "Jesus", "Garcia", "no tengo email", "956657894", "titleJesus", javax.validation.ConstraintViolationException.class
			}, {
				//Editar usuario y dejar nombre en blanco
				"chapter1", "", "Garcia", "jesus@mail.com", "956657894", "titleJesus", javax.validation.ConstraintViolationException.class
			}, {
				//Editar usuario y dejar apellido en blanco
				"chapter1", "Jesus", "", "jesus@mail.com", "956657894", "titleJesus", javax.validation.ConstraintViolationException.class
			}, {
				//Editar usuario y dejar title en blanco
				"chapter1", "Jesus", "Garcia", "jesus@mail.com", "956657894", "", javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditChapter((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateEditChapter(final String username, final String name, final String surname, final String email, final String phone, final String title, final Class<?> expected) {
		Class<?> caught;
		Chapter chapter;
		chapter = this.chapterService.findOne(this.getEntityId("chapter1"));

		caught = null;
		try {
			super.authenticate(username);
			chapter.setName(name);
			chapter.setSurname(surname);
			chapter.setEmail(email);
			chapter.setPhone(phone);
			chapter.setTitle(title);
			this.unauthenticate();
			//			this.chapterService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);

	}

}
