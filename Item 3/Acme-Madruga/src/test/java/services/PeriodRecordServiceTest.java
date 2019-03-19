
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
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
	public void test() {
		Assert.isNull(null);
	}

	@Test
	public void driverCreateSave() {
		final Collection<String> photosVacio = new ArrayList<String>();
		final Collection<String> photosElementoVacio = new ArrayList<String>();
		final Collection<String> photos = new ArrayList<String>();
		photos.add("http://tyniurl.com/dsfrefd.png");
		photos.add("http://tyniurl.com/dsfes3rfw45d.png");
		photosElementoVacio.add("");
		final Object testingData[][] = {
			{
				//Correcto
				"brotherhood1", "PeriodTest", "descriptionTest", 2018, 2023, null, null
			}, {
				//Crear con usuario distinto a brothethood
				"member1", "PeriodTest", "descriptionTest", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Title cadena vacia
				"brotherhood1", "", "descriptionTest", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Title null
				"brotherhood1", null, "descriptionTest", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Discription cadena vacia
				"brotherhood1", "PeriodTest", "", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Description null
				"brotherhood1", "PeriodTest", null, 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Photos 
				"brotherhood1", "PeriodTest", "descriptionTest", 2018, 2023, photos, null
			//	}, {
			//						//Photos coleccion vacia
			//						"brotherhood1", "PeriodTest", "descrptionTest", photosVacio, IllegalArgumentException.class
			//					}, {
			//						//Photos con elemento con URL vacía
			//						"brotherhood1", "PeriodTest", "descrptionTest", photosElementoVacio, IllegalArgumentException.class
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
			//			incRec.setPhotos(photos);
			final PeriodRecord pRecSaved = this.periodRecordService.save(pRec);
			Assert.isTrue(pRecSaved.getId() != 0);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Collection<String> photosVacio = new ArrayList<String>();
		final Collection<String> photosElementoVacio = new ArrayList<String>();
		final Collection<String> photos = new ArrayList<String>();
		photos.add("http://tyniurl.com/dsfrefd.png");
		photos.add("http://tyniurl.com/dsfes3rfw45d.png");
		photosElementoVacio.add("");
		final Object testingData[][] = {
			{
				//Correcto
				"brotherhood1", 2213, "PeriodTest", "descriptionTest", 2018, 2023, null, null
			}, {
				//Usuario al que no le pertenece este miscellaneousRecord
				"brotherhood2", 2213, "PeriodTest", "descriptionTest", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Crear con usuario distinto a brothethood
				"member1", 2213, "PeriodTest", "descriptionTest", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Title cadena vacia
				"brotherhood1", 2213, "", "descriptionTest", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Title null
				"brotherhood1", 2213, null, "descriptionTest", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Discription cadena vacia
				"brotherhood1", 2213, "PeriodTest", "", 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Description null
				"brotherhood1", 2213, "PeriodTest", null, 2018, 2023, null, IllegalArgumentException.class
			}, {
				//Photos 
				"brotherhood1", 2213, "PeriodTest", "descriptionTest", 2018, 2023, photos, null
			//	}, {
			//						//Photos coleccion vacia
			//						"brotherhood1", "PeriodTest", "descrptionTest", photosVacio, IllegalArgumentException.class
			//					}, {
			//						//Photos con elemento con URL vacía
			//						"brotherhood1", "PeriodTest", "descrptionTest", photosElementoVacio, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (Integer) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Collection<String>) testingData[i][6],
				(Class<?>) testingData[i][7]);

	}

	private void templateEdit(final String user, final Integer id, final String title, final String description, final Integer startYear, final Integer endYear, final Collection<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final PeriodRecord bPRec = this.periodRecordService.findOne(id);
			bPRec.setTitle(title);
			bPRec.setDescription(description);
			bPRec.setStartYear(startYear);
			bPRec.setEndYear(endYear);
			if (photos != null)
				bPRec.setPhotos(photos);
			this.periodRecordService.save(bPRec);
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
				"brotherhood1", 2213, null
			}, {
				"brotherhood2", 2213, IllegalArgumentException.class
			}, {
				"brotherhood1", null, IllegalArgumentException.class
			}, {
				"member1", null, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateDelete(final String actor, final Integer id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			PeriodRecord pRec;
			if (id != null)
				pRec = this.periodRecordService.findOne(id);
			else
				pRec = new PeriodRecord();
			this.periodRecordService.delete(pRec);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
