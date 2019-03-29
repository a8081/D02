
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
	public void driverCreateSave1() {
		final Collection<String> lawsVacio = new ArrayList<String>();
		final Collection<String> laws = new ArrayList<String>();
		laws.add("LawTest1");
		laws.add("LawTest2");
		final Object testingData[][] = {
			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood crea LegalRecord con coleccion de laws vacia
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, lawsVacio, null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta crear una LegalRecord
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"member1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood crea LegalRecord con coleccion de laws con datos
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, laws, null
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Double) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	@Test
	public void driverCreateSave2() {
		final Object testingData[][] = {
			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LegalRecord con title vacio
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood1", "", "descriptionTest", "Legal Record Test", 0.21, null, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LegalRecord con title null
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood1", null, "descriptionTest", "Legal Record Test", 0.21, null, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea LegalRecord con description vacia
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood1", "LegalTest", "", "Legal Record Test", 0.21, null, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea LegalRecord con description null
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood1", "LegalTest", null, "Legal Record Test", 0.21, null, ConstraintViolationException.class
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
			this.legalRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Collection<String> lawsVacio = new ArrayList<String>();
		final Collection<String> laws = new ArrayList<String>();
		laws.add("LawTest1");
		laws.add("LawTest2");
		final Object testingData[][] = {
			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood edita LegalRecord con laws vacio
				//			C: 100% Recorre 65 de las 65 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood2", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, lawsVacio, null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood edita LegalRecord con laws con datos
				//			C: 100% Recorre 65 de las 65 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood2", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, laws, null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta editar LegalRecord
				//			C: 12,3% Recorre 8 de las 65 lineas posibles
				//			D: cobertura de datos=6/405
				"member1", "LegalTest", "descriptionTest", "Legal Record Test", 0.21, null, IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LegalRecord con title vacio
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood2", "", "descriptionTest", "Legal Record Test", 0.21, null, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LegalRecord con title null
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood2", null, "descriptionTest", "Legal Record Test", 0.21, null, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LegalRecord con description vacio
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood2", "LegalTest", "", "Legal Record Test", 0.21, null, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LegalRecord con description null
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=6/405
				"brotherhood2", "LegalTest", null, "Legal Record Test", 0.21, null, ConstraintViolationException.class
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
			final ArrayList<LegalRecord> lRecs = new ArrayList<LegalRecord>(principal.getHistory().getLegalRecords());
			final LegalRecord lR = lRecs.get(0);
			lR.setTitle(title);
			lR.setDescription(description);
			lR.setLegalName(legalName);
			lR.setVat(vat);
			if (laws != null)
				lR.setLaws(laws);
			this.legalRecordService.save(lR);
			this.legalRecordService.flush();
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
				//			B: Test Positivo: Brotherhood borra LegalRecord 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"brotherhood2", null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Member intenta borrar LegalRecord 
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
			final Brotherhood bro = this.brotherhoodService.findByPrincipal();
			final ArrayList<LegalRecord> lRecs = new ArrayList<LegalRecord>(bro.getHistory().getLegalRecords());
			final LegalRecord lRec = lRecs.get(0);
			this.legalRecordService.delete(lRec);
			this.legalRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
