
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.LinkRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class LinkRecordServiceTest extends AbstractTest {

	// Services
	@Autowired
	private LinkRecordService	linkRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	@Test
	public void test() {
		Assert.isNull(null);
	}

	@Test
	public void driverCreateSave() {
		final Brotherhood bro = this.brotherhoodService.findOne(2199);
		final Object testingData[][] = {
			{
				//Correcto
				"brotherhood1", "LinkTest", "descriptionTest", bro, null
			}, {
				//Crear con usuario distinto a brothethood
				"member1", "LinkTest", "descriptionTest", bro, IllegalArgumentException.class
			}, {
				//Title cadena vacia
				"brotherhood1", "", "descriptionTest", bro, IllegalArgumentException.class
			}, {
				//Title null
				"brotherhood1", null, "descriptionTest", bro, IllegalArgumentException.class
			}, {
				//Discription cadena vacia
				"brotherhood1", "LinkTest", "", bro, IllegalArgumentException.class
			}, {
				//Description null
				"brotherhood1", "LinkTest", null, bro, IllegalArgumentException.class
			}, {
				//Brotherhood null
				"brotherhood1", "LinkTest", "descriptionTest", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Brotherhood) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void templateCreateSave(final String user, final String title, final String description, final Brotherhood bro, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final LinkRecord lRec = this.linkRecordService.create();
			lRec.setTitle(title);
			lRec.setDescription(description);
			lRec.setLinkedBrotherhood(bro);
			final LinkRecord lRecSaved = this.linkRecordService.save(lRec);
			Assert.isTrue(lRecSaved.getId() != 0);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Brotherhood bro = this.brotherhoodService.findOne(2199);
		final Object testingData[][] = {
			{
				//Correcto
				"brotherhood1", 2212, "LinkTest", "descriptionTest", bro, null
			}, {
				//Usuairo al que no le pertenece esta linkRecord
				"brotherhood2", 2212, "LinkTest", "descriptionTest", bro, IllegalArgumentException.class
			}, {
				//Crear con usuario distinto a brothethood
				"member1", 2212, "LinkTest", "descriptionTest", bro, IllegalArgumentException.class
			}, {
				//Title cadena vacia
				"brotherhood1", 2212, "", "descriptionTest", bro, IllegalArgumentException.class
			}, {
				//Title null
				"brotherhood1", 2212, null, "descriptionTest", bro, IllegalArgumentException.class
			}, {
				//Discription cadena vacia
				"brotherhood1", 2212, "LinkTest", "", bro, IllegalArgumentException.class
			}, {
				//Description null
				"brotherhood1", 2212, "LinkTest", null, bro, IllegalArgumentException.class
			}, {
				//Brotherhood null
				"brotherhood1", 2212, "LinkTest", "descriptionTest", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (Integer) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Brotherhood) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	private void templateEdit(final String user, final Integer id, final String title, final String description, final Brotherhood bro, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final LinkRecord lRec = this.linkRecordService.findOne(id);
			lRec.setTitle(title);
			lRec.setDescription(description);
			lRec.setLinkedBrotherhood(bro);
			this.linkRecordService.save(lRec);
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
				"brotherhood1", 2212, null
			}, {
				"brotherhood2", 2212, IllegalArgumentException.class
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
			LinkRecord lRec;
			if (id != null)
				lRec = this.linkRecordService.findOne(id);
			else
				lRec = new LinkRecord();
			this.linkRecordService.delete(lRec);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
