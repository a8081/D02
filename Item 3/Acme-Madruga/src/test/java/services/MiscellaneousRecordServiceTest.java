
package services;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
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
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void templateEdit(final String user, final String title, final String description, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Brotherhood principal = this.brotherhoodService.findByPrincipal();
			final ArrayList<MiscellaneousRecord> broPerRecs = new ArrayList<MiscellaneousRecord>(principal.getHistory().getMiscellaneousRecords());
			final MiscellaneousRecord bPRec = broPerRecs.get(0);
			bPRec.setTitle(title);
			bPRec.setDescription(description);
			this.miscellaneousRecordService.save(bPRec);
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
				"brotherhood1", null, null
			}, {
				"brotherhood1", new MiscellaneousRecord(), IllegalArgumentException.class
			}, {
				"member1", null, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (MiscellaneousRecord) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateDelete(final String actor, final MiscellaneousRecord miscellaneousRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			MiscellaneousRecord pRec = miscellaneousRecord;
			if (pRec == null) {
				final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
				final ArrayList<MiscellaneousRecord> pRecs = new ArrayList<MiscellaneousRecord>(brotherhood.getHistory().getMiscellaneousRecords());
				pRec = pRecs.get(0);
			}
			this.miscellaneousRecordService.delete(pRec);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
