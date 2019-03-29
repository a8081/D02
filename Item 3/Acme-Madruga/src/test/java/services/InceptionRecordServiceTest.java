
package services;

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
import domain.History;
import domain.InceptionRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class InceptionRecordServiceTest extends AbstractTest {

	// Services
	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	@Test
	public void test() {
		Assert.isNull(null);
	}

	@Test
	public void driverCreateSave() {
		final Object testingData[][] = {
			{
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Positivo: Brotherhood crea InceptionRecord
				//			C: 100% Recorre 46 de las 46 lineas posibles
				//			D: cobertura de datos=3/27
				"brotherhood1", "title inception recor 1", "description inception recor 1", null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta crear una InceptionRecord
				//			C: 28,26% Recorre 13 de las 46 lineas posibles
				//			D: cobertura de datos=3/27
				"member1", "title inception record 1", "description inception recor 1", IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita InceptionRecord con title vacio
				//			C: 97,82% Recorre 45 de las 46 lineas posibles
				//			D: cobertura de datos=3/27
				"brotherhood1", "", "descriptionTest", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea InceptionRecord con title null
				//			C: 97,82% Recorre 45 de las 46 lineas posibles
				//			D: cobertura de datos=3/27
				"brotherhood1", null, "description", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea InceptionRecord con description vacio
				//			C: 97,82% Recorre 45 de 46 lineas posibles
				//			D: cobertura de datos=3/27
				"brotherhood1", "InceptionTest", "", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood crea InceptionRecord con description null
				//			C: 97,82% Recorre 45 de 46 lineas posibles
				//			D: cobertura de datos=3/27
				"brotherhood1", "InceptionTest", null, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	protected void templateCreateSave(final String user, final String title, final String description, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final InceptionRecord incRec = this.inceptionRecordService.create();
			incRec.setTitle(title);
			incRec.setDescription(description);
			final InceptionRecord incRecSaved = this.inceptionRecordService.save(incRec);
			Assert.isTrue(incRecSaved.getId() != 0);
			this.inceptionRecordService.flush();
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
				//			B: Test Positivo: Brotherhood edita InceptionRecord
				//			C: 100% Recorre 68 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"brotherhood1", false, "title inception recor test", "description inception recor test", null
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood intenta editar Inception que no le pertenece
				//			C: 89,70% Recorre 61 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"brotherhood2", true, "title inception recor test", "description inception recor test", IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Un member intenta editar una InceptionRecord
				//			C: 11,76% Recorre 8 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"member1", false, "title inception recor test", "description inception recor test", IllegalArgumentException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita InceptionRecord con title vacio
				//			C: 98,52% Recorre 67 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"brotherhood1", false, "", "description inception recor test", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita InceptionRecord con title null
				//			C: 98,52% Recorre 67 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"brotherhood1", false, null, "description inception recor test", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita InceptionRecord con description vacio
				//			C: 98,52% Recorre 67 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"brotherhood1", false, "title inception recor test", "", ConstraintViolationException.class
			}, {
				//			A: Acme Parade Req. 3 -> Brotherhoods can manage their history
				//			B: Test Negativo: Brotherhood edita InceptionRecord con description null
				//			C: 98,52% Recorre 67 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"brotherhood1", false, "title inception recor test", null, ConstraintViolationException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (Boolean) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void templateEdit(final String actor, final Boolean prop, final String title, final String description, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final Brotherhood principal = this.brotherhoodService.findByPrincipal();
			final InceptionRecord iR;
			final History history = principal.getHistory();
			if (!prop)
				iR = history.getInceptionRecord();
			else {
				final Integer id = this.getEntityId("inceptionRecord1");
				iR = this.inceptionRecordService.findOne(id);
			}
			iR.setTitle(title);
			iR.setDescription(description);
			history.setInceptionRecord(iR);
			this.inceptionRecordService.save(iR);
			this.inceptionRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
