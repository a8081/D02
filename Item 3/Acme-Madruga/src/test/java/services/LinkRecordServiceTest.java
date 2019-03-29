
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
	public void driverCreateSave() {
		final Collection<Brotherhood> bros = this.brotherhoodService.findAll();
		Brotherhood bro = null;
		for (final Brotherhood b : bros)
			if (b.getMiddleName().contains("Triana"))
				bro = b;
		final Object testingData[][] = {
			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood crea LinkRecord
				//			C: 100% Recorre 46 de las 46 lineas posibles
				//			D: cobertura de datos=4/27
				"brotherhood2", "LinkTest", "descriptionTest", bro, null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta crear una LinkRecord
				//			C: 27% Recorre 13 de las 46 lineas posibles
				//			D: cobertura de datos=4/27
				"member1", "LinkTest", "descriptionTest", bro, IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LinkRecord con title vacio
				//			C: 97,91% Recorre 47 de las 48 lineas posibles
				//			D: cobertura de datos=4/27

				"brotherhood2", "", "descriptionTest", bro, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LinkRecord con title null
				//			C: 97,91% Recorre 47 de las 48 lineas posibles
				//			D: cobertura de datos=4/27
				"brotherhood2", null, "descriptionTest", bro, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LinkRecord con description vacio
				//			C: 97,91% Recorre 47 de las 48 lineas posibles
				//			D: cobertura de datos=4/27

				"brotherhood2", "LinkTest", "", bro, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LinkRecord con description null
				//			C: 97,91% Recorre 47 de las 48 lineas posibles
				//			D: cobertura de datos=4/27

				"brotherhood2", "LinkTest", null, bro, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LinkRecord con linkedBrotherhood null
				//			C: 97,91% Recorre 47 de las 48 lineas posibles
				//			D: cobertura de datos=4/27

				"brotherhood2", "LinkTest", "descriptionTest", null, ConstraintViolationException.class
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
			this.linkRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Collection<Brotherhood> bros = this.brotherhoodService.findAll();
		Brotherhood bro = null;
		for (final Brotherhood b : bros)
			if (b.getMiddleName().contains("Triana"))
				bro = b;
		final Object testingData[][] = {
			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood edita LinkRecord
				//			C: 100% Recorre 65 de las 65 lineas posibles
				//			D: cobertura de datos=4/27
				"brotherhood2", "LinkTest", "descriptionTest", bro, null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta editar LinkRecord
				//			C: 12,3% Recorre 8 de las 65 lineas posibles
				//			D: cobertura de datos=4/27
				"member1", "LinkTest", "descriptionTest", bro, IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LinkRecord con title vacio
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=4/27
				"brotherhood2", "", "descriptionTest", bro, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LinkRecord con title null
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=4/27
				"brotherhood2", null, "descriptionTest", bro, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LinkRecord con description vacio
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=4/27
				"brotherhood2", "LinkTest", "", bro, ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita LinkRecord con description null
				//			C: 98,4% Recorre 64 de las 65 lineas posibles
				//			D: cobertura de datos=4/27
				"brotherhood2", "LinkTest", null, bro, ConstraintViolationException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Brotherhood) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	private void templateEdit(final String user, final String title, final String description, final Brotherhood bro, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Brotherhood principal = this.brotherhoodService.findByPrincipal();
			final ArrayList<LinkRecord> lRecs = new ArrayList<LinkRecord>(principal.getHistory().getLinkRecords());
			final LinkRecord lRec = lRecs.get(0);
			lRec.setTitle(title);
			lRec.setDescription(description);
			lRec.setLinkedBrotherhood(bro);
			this.linkRecordService.save(lRec);
			this.linkRecordService.flush();
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
				//			B: Test Positivo: Brotherhood borra LinkRecord 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"brotherhood2", null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Member intenta borrar LinkRecord 
				//			C: 100% Recorre 8 de las 78 lineas posibles
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
			final ArrayList<LinkRecord> lRecs = new ArrayList<LinkRecord>(principal.getHistory().getLinkRecords());
			final LinkRecord lRec = lRecs.get(0);
			this.linkRecordService.delete(lRec);
			this.linkRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
