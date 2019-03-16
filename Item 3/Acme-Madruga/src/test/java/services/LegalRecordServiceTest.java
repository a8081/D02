
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
import domain.Brotherhood;
import domain.LegalRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class LegalRecordServiceTest extends AbstractTest {

	// Services
	@Autowired
	private LegalRecordService	legalRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	@Test
	public void test() {
		Assert.isNull(null);
	}

	@Test
	public void driverCreateSave() {
		final Collection<String> laws = new ArrayList<String>();
		laws.add("LawTest1");
		laws.add("LawTest2");
		final Object testingData[][] = {
			{
				//Correcto
				"brotherhood1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, null, null
			}, {
				//Crear con usuario distinto a brothethood
				"member1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Title cadena vacia
				"brotherhood1", "", "descriptionTest", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Title null
				"brotherhood1", null, "descriptionTest", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Discription cadena vacia
				"brotherhood1", "LegalTest", "", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Description null
				"brotherhood1", "LegalTest", null, "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Laws
				"brotherhood1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, laws, null
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Double) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	protected void templateCreateSave(final String user, final String title, final String description, final String legalName, final Double vat, final Collection<String> laws, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final LegalRecord lRec = this.legalRecordService.create();
			lRec.setTitle(title);
			lRec.setDescription(description);
			lRec.setLegalName(legalName);
			lRec.setVat(vat);
			if (laws != null)
				lRec.setLaws(laws);
			final LegalRecord lRecSaved = this.legalRecordService.save(lRec);
			Assert.isTrue(lRecSaved.getId() != 0);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Collection<String> laws = new ArrayList<String>();
		laws.add("LawTest1");
		laws.add("LawTest2");
		final Object testingData[][] = {
			{
				//Correcto
				"brotherhood1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, null, null
			}, {
				//Crear con usuario distinto a brothethood
				"member1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Title cadena vacia
				"brotherhood1", "", "descriptionTest", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Title null
				"brotherhood1", null, "descriptionTest", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Discription cadena vacia
				"brotherhood1", "LegalTest", "", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Description null
				"brotherhood1", "LegalTest", null, "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//Laws
				"brotherhood1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, laws, null
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Double) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateEdit(final String user, final String title, final String description, final String legalName, final Double vat, final Collection<String> laws, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Brotherhood principal = this.brotherhoodService.findByPrincipal();
			final ArrayList<LegalRecord> broLegRecs = new ArrayList<LegalRecord>(principal.getHistory().getLegalRecords());
			final LegalRecord bLRec = broLegRecs.get(0);
			bLRec.setTitle(title);
			bLRec.setDescription(description);
			bLRec.setLegalName(legalName);
			bLRec.setVat(vat);
			if (laws != null)
				bLRec.setLaws(laws);
			this.legalRecordService.save(bLRec);
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
				"brotherhood1", new LegalRecord(), IllegalArgumentException.class
			}, {
				"member1", null, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (LegalRecord) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateDelete(final String actor, final LegalRecord legalRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			LegalRecord lRec = legalRecord;
			if (lRec == null) {
				final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
				final ArrayList<LegalRecord> lRecs = new ArrayList<LegalRecord>(brotherhood.getHistory().getLegalRecords());
				lRec = lRecs.get(0);
			}
			this.legalRecordService.delete(lRec);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
