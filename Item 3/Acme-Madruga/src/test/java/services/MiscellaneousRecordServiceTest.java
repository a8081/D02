
package services;

import java.util.ArrayList;

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
	public void driverCreateSave() {
		final Object testingData[][] = {
			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood crea MiscellaneousRecord
				//			C: 100% Recorre 41 de las 41 lineas posibles
				//			D: cobertura de datos=3/9
				"brotherhood1", "MiscellaneousTest", "descriptionTest", null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta crear una MiscellaneousRecord
				//			C: 29,39% Recorre 12 de las 41 lineas posibles
				//			D: cobertura de datos=3/9
				"member1", "MiscellaneousTest", "descriptionTest", IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea MiscellaneousRecord con title vacio
				//			C: 97,5% Recorre 40 de las 41 lineas posibles
				//			D: cobertura de datos=3/9

				"brotherhood1", "", "descriptionTest", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea MiscellaneousRecord con title null
				//			C: 97,5% Recorre 40 de las 41 lineas posibles
				//			D: cobertura de datos=3/9
				"brotherhood1", null, "descriptionTest", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea MiscellaneousRecord con description vacio
				//			C: 97,5% Recorre 40 de las 41 lineas posibles
				//			D: cobertura de datos=3/9
				"brotherhood1", "MiscellaneousTest", "", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea MiscellaneousRecord con description null
				//			C: 97,5% Recorre 40 de las 41 lineas posibles
				//			D: cobertura de datos=3/9
				"brotherhood1", "MiscellaneousTest", null, ConstraintViolationException.class
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
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood edita MiscellaneousRecord
				//			C: 100% Recorre 61 de las 61 lineas posibles
				//			D: cobertura de datos=3/9
				"brotherhood2", "MiscellaneousTest", "descriptionTest", null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta editar MiscellaneousRecord
				//			C: 13,11% Recorre 8 de las 61 lineas posibles
				//			D: cobertura de datos=3/9
				"member1", "MiscellaneousTest", "descriptionTest", IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita MiscellaneousRecord con title vacio
				//			C: 98,36% Recorre 60 de 61 lineas posibles
				//			D: cobertura de datos=3/9
				"brotherhood2", "", "descriptionTest", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita MiscellaneousRecord con title null
				//			C: 98,36% Recorre 60 de las 61 lineas posibles
				//			D: cobertura de datos=3/9
				"brotherhood2", null, "descriptionTest", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita MiscellaneousRecord con description vacio
				//			C: 98,36% Recorre 60 de las 61 lineas posibles
				//			D: cobertura de datos=3/9
				"brotherhood2", "MiscellaneousTest", "", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita MiscellaneousRecord con description null
				//			C: 98,36% Recorre 60 de 61 lineas posibles
				//			D: cobertura de datos=3/9
				"brotherhood2", "MiscellaneousTest", null, ConstraintViolationException.class
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
			final ArrayList<MiscellaneousRecord> mRecs = new ArrayList<MiscellaneousRecord>(principal.getHistory().getMiscellaneousRecords());
			final MiscellaneousRecord mRec = mRecs.get(0);
			mRec.setTitle(title);
			mRec.setDescription(description);
			this.miscellaneousRecordService.save(mRec);
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
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood borra MiscellaneousRecord 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"brotherhood2", null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Member intenta borrar MiscellaneousRecord 
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
			final Brotherhood principal = this.brotherhoodService.findByPrincipal();
			final ArrayList<MiscellaneousRecord> mRecs = new ArrayList<MiscellaneousRecord>(principal.getHistory().getMiscellaneousRecords());
			final MiscellaneousRecord mRec = mRecs.get(0);
			this.miscellaneousRecordService.delete(mRec);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
