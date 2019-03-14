
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.GPS;
import domain.Parade;
import domain.Segment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SegmentServiceTest extends AbstractTest {

	@Autowired
	private SegmentService		segmentService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ParadeService		paradeService;


	/* ========================= Test Create and Save Chapter =========================== */

	@Test
	public void driverCreateAndSaveSegment() {
		final Object testingData[][] = {
			{
				// Crear segment correctamente
				"2018/03/16 15:20", "2018/03/16 15:40", "gps1", "gps2", null
			}, {
				//Crear segment con parametro incorrecto
				"2018/03/16 15:40", "2018/03/16 17:00", "gps21", "gpsFinal", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((Date) testingData[i][0], (Date) testingData[i][1], (GPS) testingData[i][2], (GPS) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void templateCreateAndSave(final Date originTime, final Date destinationTime, final GPS originCoordinates, final GPS destinationCoordinates, final Class<?> expected) {

		Class<?> caught;
		Segment segment;
		final Integer brotherhoodId = this.getEntityId("brotherhood1");
		final Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		final List<Parade> parades = new ArrayList<>(this.paradeService.findAllParadeByBrotherhoodId(brotherhood.getUserAccount().getId()));

		final Parade parade = this.paradeService.findOne(parades.get(0).getId());

		caught = null;

		try {
			this.authenticate("brotherhood1");
			segment = this.segmentService.create();
			segment.setOriginTime(originTime);
			segment.getDestinationTime();
			segment.getOriginCoordinates();
			segment.getDestinationCoordinates();
			segment = this.segmentService.save(segment, parade.getId());
			this.segmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();
	}
	/* ========================= Test Edit Chapter =========================== */
	//
	//	@Test
	//	public void driverEditChapter() {
	//
	//		final Object testingData[][] = {
	//			{
	//				// Editar tus datos
	//				"chapter1", "Name chapter 1", "Surname chapter 1", "chapter1@hotmail.es", "+34655398675", "Title chapter 1", null
	//			}, {
	//				//Editar phone vacio
	//				"chapter1", "Name chapter 1", "Surname chapter 1", "chapter1@hotmail.es", "", "Title chapter 1", null
	//			}, {
	//				//Editar email incorrecto
	//				"chapter1", "Name chapter 1", "Surname chapter 1", "no tengo email", "+34655398675", "Title chapter 1", ConstraintViolationException.class
	//			}, {
	//				//Editar usuario y dejar nombre en blanco
	//				"chapter1", "", "Surname chapter 1", "chapter1@hotmail.es", "+34655398675", "Title chapter 1", ConstraintViolationException.class
	//			}, {
	//				//Editar usuario y dejar apellido en blanco
	//				"chapter1", "Name chapter 1", "", "chapter1@hotmail.es", "+34655398675", "Title chapter 1", ConstraintViolationException.class
	//			}, {
	//				//Editar usuario y dejar title en blanco
	//				"chapter1", "Name chapter 1", "Surname chapter 1", "chapter1@hotmail.es", "+34655398675", "", ConstraintViolationException.class
	//			}
	//		};
	//		for (int i = 0; i < testingData.length; i++)
	//			this.templateEditChapter((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	//	}
	//
	//	private void templateEditChapter(final String username, final String name, final String surname, final String email, final String phone, final String title, final Class<?> expected) {
	//		Class<?> caught;
	//		Chapter chapter;
	//		chapter = this.chapterService.findOne(this.getEntityId(username));
	//
	//		caught = null;
	//		try {
	//			super.authenticate(username);
	//			chapter.setName(name);
	//			chapter.setSurname(surname);
	//			chapter.setEmail(email);
	//			chapter.setPhone(phone);
	//			chapter.setTitle(title);
	//			this.unauthenticate();
	//			this.chapterService.flush();
	//
	//		} catch (final Throwable oops) {
	//			caught = oops.getClass();
	//
	//		}
	//
	//		this.checkExceptions(expected, caught);
	//
	//	}

}
