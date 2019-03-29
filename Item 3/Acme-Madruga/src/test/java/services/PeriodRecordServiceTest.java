
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
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.PeriodRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PeriodRecordServiceTest extends AbstractTest {

	// Services
	@Autowired
	private PeriodRecordService	periodRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	@Test
	public void driverCreateSave1() {
		final Collection<String> photosVacio = new ArrayList<String>();
		final Collection<String> photos = new ArrayList<String>();
		photos.add("http://tyniurl.com/dsfrefd.png");
		photos.add("http://tyniurl.com/dsfes3rfw45d.png");
		final Object testingData[][] = {

			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood crea PeriodRecord con coleccion de photos vacia
				//			C: 100% Recorre 48 de las 48 lineas posibles
				//			D: cobertura de datos=6/72
				"brotherhood1", "PeriodTest", "descriptionTest", 2018, 2023, photosVacio, null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta crear una PeriodRecord
				//			C: 31,25% Recorre 15 de las 48 lineas posibles
				//			D: cobertura de datos=6/72
				"member1", "PeriodTest", "descriptionTest", 2018, 2023, photosVacio, IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood crea PeriodRecord con coleccion de photos con datos
				//			C: 100% Recorre 48 de las 48 lineas posibles
				//			D: cobertura de datos=6/72
				"brotherhood1", "PeriodTest", "descriptionTest", 2018, 2023, photos, null
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	@Test
	public void driverCreateSave2() {
		final Collection<String> photosVacio = new ArrayList<String>();
		final Collection<String> photos = new ArrayList<String>();
		photos.add("http://tyniurl.com/dsfrefd.png");
		photos.add("http://tyniurl.com/dsfes3rfw45d.png");
		final Object testingData[][] = {

			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita PeriodRecord con title vacio
				//			C: 97,91% Recorre 47 de las 48 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood1", "", "descriptionTest", 2018, 2023, photosVacio, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita PeriodRecord con title null
				//			C: 97,91% Recorre 47 de las 48 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood1", null, "descriptionTest", 2018, 2023, photosVacio, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea PeriodRecord con description vacia
				//			C: 97,91% Recorre 47 de las 48 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood1", "PeriodTest", "", 2018, 2023, photosVacio, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea PeriodRecord con description null
				//			C: 97,91% Recorre 47 de las 48 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood1", "PeriodTest", null, 2018, 2023, photosVacio, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	protected void templateCreateSave(final String user, final String title, final String description, final Integer startYear, final Integer endYear, final Collection<String> photos, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final PeriodRecord pRec = this.periodRecordService.create();
			pRec.setTitle(title);
			pRec.setDescription(description);
			pRec.setStartYear(startYear);
			pRec.setEndYear(endYear);
			if (photos != null)
				pRec.setPhotos(photos);
			final PeriodRecord pRecSaved = this.periodRecordService.save(pRec);
			Assert.isTrue(pRecSaved.getId() != 0);
			this.periodRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Collection<String> photosVacio = new ArrayList<String>();
		final Collection<String> photos = new ArrayList<String>();
		photos.add("http://tyniurl.com/dsfrefd.png");
		photos.add("http://tyniurl.com/dsfes3rfw45d.png");

		final Object testingData[][] = {
			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood edita PeriodRecord con photos vacio
				//			C: 100% Recorre 65 de las 65 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood2", "PeriodTest", "descriptionTest", 2018, 2023, photosVacio, null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood edita PeriodRecord con photos con datos
				//			C: 100% Recorre 65 de las 65 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood2", "PeriodTest", "descriptionTest", 2018, 2023, photos, null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta editar PeriodRecord
				//			C: 12,30% Recorre 8 de las 65 lineas posibles
				//			D: cobertura de datos=6/216
				"member1", "PeriodTest", "descriptionTest", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita PeriodRecord con title vacio
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood2", "", "descriptionTest", 2018, 2023, null, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita PeriodRecord con title vacio
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood2", null, "descriptionTest", 2018, 2023, null, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita PeriodRecord con description vacio
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood2", "PeriodTest", "", 2018, 2023, null, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita PeriodRecord con description null
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=6/216
				"brotherhood2", "PeriodTest", null, 2018, 2023, null, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);

	}
	private void templateEdit(final String user, final String title, final String description, final Integer startYear, final Integer endYear, final Collection<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Brotherhood bro = this.brotherhoodService.findByPrincipal();
			final ArrayList<PeriodRecord> pRecs = new ArrayList<PeriodRecord>(bro.getHistory().getPeriodRecords());
			final PeriodRecord bPRec = pRecs.get(0);
			bPRec.setTitle(title);
			bPRec.setDescription(description);
			bPRec.setStartYear(startYear);
			bPRec.setEndYear(endYear);
			if (photos != null)
				bPRec.setPhotos(photos);
			this.periodRecordService.save(bPRec);
			this.periodRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
	@Test
	public void driverDelete() {

		final Object testingData[][] = {
			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood borra PeriodRecord 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"brotherhood2", null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Member intenta borrar PeriodRecord 
				//			C: 10,25% Recorre 8 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"member1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateDelete(final String actor, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final PeriodRecord bPRec;
			final Brotherhood bro = this.brotherhoodService.findByPrincipal();
			final ArrayList<PeriodRecord> pRecs = new ArrayList<PeriodRecord>(bro.getHistory().getPeriodRecords());
			bPRec = pRecs.get(0);
			this.periodRecordService.delete(bPRec);
			this.periodRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
