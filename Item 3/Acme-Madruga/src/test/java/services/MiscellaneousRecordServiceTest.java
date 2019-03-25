
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.MiscellaneousRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	// Services
	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private BrotherhoodService			brotherhoodService;


	@Test
	public void test() {
		Assert.isNull(null);
	}

	@Test
	public void driverCreateSave() {
		final Object testingData[][] = {
			{
				//Correcto
				"brotherhood1", "MiscellaneousTest", "descriptionTest", null
			}, {
				//Crear con usuario distinto a brothethood
				"member1", "MiscellaneousTest", "descriptionTest", IllegalArgumentException.class
			}, {
				//Title cadena vacia
				"brotherhood1", "", "descriptionTest", IllegalArgumentException.class
			}, {
				//Title null
				"brotherhood1", null, "descriptionTest", IllegalArgumentException.class
			}, {
				//Discription cadena vacia
				"brotherhood1", "MiscellaneousTest", "", IllegalArgumentException.class
			}, {
				//Description null
				"brotherhood1", "MiscellaneousTest", null, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	protected void templateCreateSave(final String user, final String title, final String description, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final MiscellaneousRecord pRec = this.miscellaneousRecordService.create();
			pRec.setTitle(title);
			pRec.setDescription(description);
			final MiscellaneousRecord pRecSaved = this.miscellaneousRecordService.save(pRec);
			Assert.isTrue(pRecSaved.getId() != 0);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			{
				//Correcto
				"brotherhood1", 2204, "MiscellaneousTest", "descriptionTest", null
			}, {
				//Usuario al que no le pertenece este miscellaneousRecord
				"brotherhood2", 2204, "MiscellaneousTest", "descriptionTest", IllegalArgumentException.class
			}, {
				//Crear con usuario distinto a brothethood
				"member1", 2204, "MiscellaneousTest", "descriptionTest", IllegalArgumentException.class
			}, {
				//Title cadena vacia
				"brotherhood1", 2204, "", "descriptionTest", IllegalArgumentException.class
			}, {
				//Title null
				"brotherhood1", 2204, null, "descriptionTest", IllegalArgumentException.class
			}, {
				//Discription cadena vacia
				"brotherhood1", 2204, "MiscellaneousTest", "", IllegalArgumentException.class
			}, {
				//Description null
				"brotherhood1", 2204, "MiscellaneousTest", null, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (Integer) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void templateEdit(final String user, final Integer id, final String title, final String description, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final MiscellaneousRecord bPRec = this.miscellaneousRecordService.findOne(id);
			bPRec.setTitle(title);
			bPRec.setDescription(description);
			this.miscellaneousRecordService.save(bPRec);
			this.miscellaneousRecordService.flush();
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
				"brotherhood1", 2204, null
			}, {
				"brotherhood2", 2204, IllegalArgumentException.class
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
			MiscellaneousRecord pRec;
			if (id != null)
				pRec = this.miscellaneousRecordService.findOne(id);
			else
				pRec = new MiscellaneousRecord();
			this.miscellaneousRecordService.delete(pRec);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
